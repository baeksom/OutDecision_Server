package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.PostListDTO;
import org.springframework.data.domain.Page;


public interface MyActivityService {

    Page<Post> getMyPostListByStatus(Long memberId, Status status, Integer page);
    Page<Post> getLikedPostListByStatus(Long memberId, Status status, Integer page);
    Page<Post> getVotedPostListByStatus(Long memberId, Status status, Integer page);

    PostDTO post(Post post);
    PostListDTO postList(Page<Post> postList);

}
