package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Gender;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.domain.MemberView;
import KGUcapstone.OutDecision.domain.user.repository.MemberViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsServiceImpl implements PostsService{

    private final PostRepository postRepository;
    private final MemberViewRepository memberViewRepository;

    @Override
    public Page<Post> getPosts(String sort,
                              String keyword,
                              String searchType,
                              Map<String, String> filters,
                              Integer page,
                              Integer size) {
        // 필터링 적용
        List<Post> filteredPosts = applyFilters(sort, keyword, searchType, filters);

        // 페이징 적용
        Pageable pageable = PageRequest.of(page, size);
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredPosts.size());
        return new PageImpl<>(filteredPosts.subList(start, end), pageable, filteredPosts.size());
    }

    private List<Post> applyFilters (String sort,
                                       String keyword,
                                       String searchType,
                                       Map<String, String> filters){

        List<Post> posts = postRepository.findAll();

        // searchType - 제목, 내용, 제목+내용
        if (keyword != null && !keyword.trim().isEmpty()) {
            // 키워드가 있다면 == 사용자가 키워드 검색을 했다면
            String[] keywords = keyword.split("\\s+"); // 공백을 기준으로 단어를 분리
            Set<Post> tempMatches = new HashSet<>(); // set으로 중복 방지

            for (String word : keywords) {
                List<Post> matches;
                if (searchType.equals("title")) {
                    matches = postRepository.findByTitleContaining(word);
                } else if (searchType.equals("content")) {
                    matches = postRepository.findByContentContaining(word);
                } else {
                    List<Post> titleMatches = postRepository.findByTitleContaining(word);
                    List<Post> contentMatches = postRepository.findByContentContaining(word);
                    // 중복 방지
                    Set<Post> temp = new HashSet<>(titleMatches);
                    temp.addAll(contentMatches);
                    matches = new ArrayList<>(temp);
                }
                if (tempMatches.isEmpty()) {
                    tempMatches.addAll(matches);
                } else {
                    tempMatches.retainAll(matches);
                }
            }
            posts = new ArrayList<>(tempMatches);
        }

        // 필터 적용 - category, mode, gender, vote
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            String filterType = entry.getKey();
            String filterValue = entry.getValue();

            if (filterValue != null) {
                if (filterType.equals("category")) {
                    // Category enum 이랑 매핑해주어야함
                    posts = findByFilter(posts, post -> post.getCategory().equals(Category.fromValue(filterValue)));
                } else if (filterType.equals("mode")) {
                    if (filterValue.equals("hot")) {
                        // 핫 게시글 - hot, normal
                        // hot이 true라면 hot 게시글
                        posts = findByFilter(posts, post -> post.getHot().equals(true));
                    }
                } else if (filterType.equals("gender")) {
                    // 성별 필터 - female, male
                    // Gender Enum 과 매핑해주어야함
                    posts = findByFilter(posts, post -> post.getGender().equals(filterValue.equals("female")? Gender.female : Gender.male));
                } else if (filterType.equals("vote")) {
                    // 투표 상태 - progress, end
                    // Status Enum 과 매핑해주어야함
                    posts = findByFilter(posts, post -> post.getStatus().equals(filterValue.equals("progress")? Status.VOTING : Status.CLOSING));
                }
            }
        }

        if (sort.equals("latest")) {
            // 초기 default는 최신순(끌어올리기 순) 정렬
            posts.sort(Comparator.comparing(Post::getBumpsTime).reversed());
        }
        else if (sort.equals("views")) {
            // 조회수 내림차순 정렬
            posts.sort(Comparator.comparing(Post::getViews).reversed());
        } else {
            // 좋아요수 내림차순 정렬
            posts.sort(Comparator.comparing(Post::getLikes).reversed());
        }

        return posts;
    }

    private List<Post> findByFilter (List<Post> posts, Predicate<Post> condition) {
        List<Post> filterPosts = new ArrayList<>();
        for (Post post : posts){
            if (condition.test(post)) {
                filterPosts.add(post);
            }
        }
        return filterPosts;
    }

    public Page<Post> getRecommendPost(Long memberId, Integer page, Integer size) {
        List<Post> recommend=recommendPost(memberId);
        // 페이징 적용
        Pageable pageable = PageRequest.of(page, size);
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), recommend.size());
        return new PageImpl<>(recommend.subList(start, end), pageable, recommend.size());
    }

    public List<Post> recommendPost(Long memberId) {

        // 사용자의 조회 기록 가져오기
        List<MemberView> viewList = memberViewRepository.findMemberViewsByMemberId(memberId);

        if (viewList == null || viewList.isEmpty()) {
            // 사용자의 조회 기록이 없는 경우, 모든 게시글을 반환
            Pageable pageable = PageRequest.of(0, 5); // 페이지와 사이즈 조정
            return postRepository.findAll(pageable).getContent();
        }

        // UserBasedCF를 사용하여 추천 시스템 실행
        int topSimilarUsers = 5; // 상위 유사 사용자의 수
        UserBasedCF userBasedCF = new UserBasedCF(viewList, topSimilarUsers);
        Map<String, Double> recommendations = userBasedCF.predictRecommendations(memberId);

        // 모든 게시글 가져오기
        List<Post> allPosts = postRepository.findAll();
        // 게시글을 추천 점수를 기준으로 내림차순으로 정렬하여 상위 10개 게시글 선택
        allPosts.sort((post1, post2) -> {
            double score1 = calculateScore(post1, recommendations);
            double score2 = calculateScore(post2, recommendations);
            return Double.compare(score2, score1); // 내림차순 정렬
        });

        // 상위 5개의 게시글 추천 리스트에 추가
        List<Post> recommendPosts = new ArrayList<>();
        int count = 0;
        for (Post post : allPosts) {
            double score = calculateScore(post, recommendations);
            if (!Double.isNaN(score)) {
                recommendPosts.add(post);
                count++;
                if (count >= 5) {
                    break;
                }
            }
        }
        // 추천된 게시글 반환
        return recommendPosts;
    }

    // 게시글의 총점을 계산하는 메소드
    private double calculateScore(Post post, Map<String, Double> recommendations) {
        // 각 카테고리별 추천 점수와 게시글의 정보를 이용하여 총점 계산
        double categoryScore = recommendations.getOrDefault(post.getCategory().toString(), 0.0);
        double likesScore = post.getLikes() * 5;
        double totalViews = post.getViews();
        // 총점 계산
        return categoryScore + likesScore + totalViews;
    }
}
