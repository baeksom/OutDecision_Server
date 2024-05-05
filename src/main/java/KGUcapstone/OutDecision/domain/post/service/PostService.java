package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.PostDTO;

public interface PostService {
    PostDTO viewPost(Long postId);
    boolean deletePost(Long postId);
}
