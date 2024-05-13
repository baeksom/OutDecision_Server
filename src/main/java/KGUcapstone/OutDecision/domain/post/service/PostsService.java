package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import org.springframework.data.domain.Page;
import java.util.Map;

public interface PostsService {

    public Page<Post> getPosts(String sort,
                               String keyword,
                               String searchType,
                               Map<String, String> filters,
                               Integer page,
                               Integer size);

    public Page<Post> getRecommendPost(Long memberId,
                                       Integer page,
                                       Integer size);
}
