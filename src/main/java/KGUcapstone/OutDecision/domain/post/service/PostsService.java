package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDto;
import org.springframework.data.domain.Page;

import java.util.Map;

import static KGUcapstone.OutDecision.domain.post.dto.PostsResponseDto.*;

public interface PostsService {

    public Page<Post> getPosts(String sort,
                               String keyword,
                               String searchType,
                               Map<String, String> filters,
                               Integer page,
                               Integer size);

    public PostDTO toPostDTO(Post post);
    public PostListDTO toPostListDTO(Page<Post> postList);
}
