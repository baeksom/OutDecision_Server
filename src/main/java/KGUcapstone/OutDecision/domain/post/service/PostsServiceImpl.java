package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Gender;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDto;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Transactional
public class PostsServiceImpl implements PostsService{

    private final PostRepository postRepository;

    @Override
    public Page<PostsResponseDto.PostDto> getPosts(String sort,
                                                   String keyword,
                                                   String searchType,
                                                   Map<String, String> filters,
                                                   Integer page,
                                                   Integer size) {
        // 필터링 적용
        List<PostsResponseDto.PostDto> filteredPosts = applyFilters(sort, keyword, searchType, filters);

        // 페이징 적용
        Pageable pageable = PageRequest.of(page, size);
        int start = (int)pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredPosts.size());
        return new PageImpl<>(filteredPosts.subList(start, end), pageable, filteredPosts.size());

    }

    public List<PostsResponseDto.PostDto> applyFilters (String sort,
                          String keyword,
                          String searchType,
                          Map<String, String> filters){
        // 초기 default는 최신순(latest) 정렬
        List<Post> posts = postRepository.findByOrderByCreatedAtDesc();

        if (sort.equals("views")) {
            // 조회수 내림차순 정렬
            posts = postRepository.findByOrderByViewsDesc();
        } else /*if (sort.equals("likes"))*/ {
            // 좋아요수 내림차순 정렬
            posts = postRepository.findByOrderByLikesDesc();
        }

        // searchType - 제목, 내용, 제목+내용
        if (keyword != null) {
            // 키워드가 있다면 == 사용자가 키워드 검색을 했다면
            if (searchType.equals("title")){
                // 제목에서 키워드 찾기
                posts = postRepository.findByTitleContaining(keyword);
            } else if (searchType.equals("content")) {
                // 내용에서 키워드 찾기
                posts = postRepository.findByContentContaining(keyword);
            } else {
                // 제목+내용에서 키워드 찾기
                posts = postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
            }
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

        // dto 생성
        List<PostsResponseDto.PostDto> postDtoList = new ArrayList<>();
        for (Post post : posts) {
            postDtoList.add(toPostDto(post));
        }

        return postDtoList;
    }

    public List<Post> findByFilter (List<Post> posts, Predicate<Post> condition) {
        List<Post> filterPosts = new ArrayList<>();
        for (Post post : posts){
            if (condition.test(post)) {
                filterPosts.add(post);
            }
        }
        return filterPosts;
    }

    public PostsResponseDto.PostDto toPostDto(Post post) {
        return PostsResponseDto.PostDto.builder()
                .id(post.getId())
                .postTitle(post.getTitle())
                .category(post.getCategory())
                .stats(post.getStatus())
                .userId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .likes(post.getLikes())
                .views(post.getViews())
                .comments(post.getCommentsList().size())
                .createAt(post.getCreatedAt())
                .build();
    }

}
