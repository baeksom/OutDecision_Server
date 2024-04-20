package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDto;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface PostsService {
    public Page<PostsResponseDto.PostDto> getPosts(String sort,
                                                   String keyword,
                                                   String searchType,
                                                   Map<String, String> filters,
                                                   Integer page,
                                                   Integer size);
}
