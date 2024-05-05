package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.PostDTO;

public interface PostService {
    /* 조회 */
    PostDTO viewPost(Long postId);

    /* 삭제 */
    boolean deletePost(Long postId);
}
