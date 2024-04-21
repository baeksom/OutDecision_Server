package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Gender;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDto;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static KGUcapstone.OutDecision.domain.post.dto.PostsResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostsServiceImpl implements PostsService{

    private final PostRepository postRepository;

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
                    posts = findByFilter(posts, post -> post.getGender().equals(filterValue.equals("female")? Gender.FEMALE : Gender.MALE));
                } else if (filterType.equals("vote")) {
                    // 투표 상태 - progress, end
                    // Status Enum 과 매핑해주어야함
                    posts = findByFilter(posts, post -> post.getStatus().equals(filterValue.equals("progress")? Status.VOTING : Status.CLOSING));
                }
            }
        }

        if (sort.equals("latest")) {
            // 초기 default는 최신순(latest) 정렬
            posts.sort(Comparator.comparing(Post::getCreatedAt).reversed());
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

    // 마감일 형식 수정하여 반환
    private String formatDeadline(Date dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return sdf.format(dateTime);
    }

    // 작성일 형식 수정하여 반환
    private String formatCreatedAt(LocalDateTime createdAt) {
        if (createdAt.toLocalDate().isEqual(LocalDate.now())) {
            // 오늘이라면 HH:mm 시간만 표시
            return createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            // 오늘이 아니라면 MM-dd 형식으로 표시
            return createdAt.format(DateTimeFormatter.ofPattern("MM-dd"));
        }
    }

    @Override
    // PostDTO 생성
    public PostDTO toPostDTO(Post post) {
        int participationCnt = ((Long) post.getOptionsList().stream()
                .flatMap(option -> option.getVoteToOptionsList().stream())
                .map(voteToOptions -> voteToOptions.getVote().getMember())
                .distinct() // 멤버 중복 제거
                .count()) // 참여자 수 계산
                .intValue();

        // 총 투표 수 계산
        long totalVoteCnt = post.getOptionsList().stream()
                .mapToLong(option -> option.getVoteToOptionsList().size())
                .sum();

        List<OptionsDTO> optionsDtoList = post.getOptionsList().stream()
                .map(option -> {
                    // 해당 option의 투표 수 계산
                    long optionVoteCnt = option.getVoteToOptionsList().size();

                    // 투표 결과 퍼센트 계산 (소수점 없음)
                    int votePercentage = (int) Math.round((optionVoteCnt * 100.0) / totalVoteCnt);

                    return OptionsDTO.builder()
                            .body(option.getBody())
                            .imgUrl(option.getPhotoUrl())
                            .votePercentage(votePercentage)
                            .build();
                })
                .toList();

        return PostDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .stats(post.getStatus())
                .userId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .pluralVoting(post.getPluralVoting())
                .createdAt(formatCreatedAt(post.getCreatedAt()))
                .deadline(formatDeadline(post.getDeadline()))
                .participationCnt(participationCnt)
                .likesCnt(post.getLikes())
                .commentsCnt(post.getCommentsList().size())
                .views(post.getViews())
                .optionsList(optionsDtoList)
                .build();
    }

    // PostListDTO 생성
    @Override
    public PostListDTO toPostListDTO(Page<Post> postList) {
        List<PostDTO> postDTOList = postList.stream()
                .map(this::toPostDTO)
                .collect(Collectors.toList());

        return PostListDTO.builder()
                .postList(postDTOList)
                .listSize(postDTOList.size())
                .totalPage(postList.getTotalPages())
                .totalElements(postList.getTotalElements())
                .isFirst(postList.isFirst())
                .isLast(postList.isLast())
                .build();
    }
}
