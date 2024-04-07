package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.MyPostDTO;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.MyPostListDTO;
import org.springframework.data.domain.Page;


public interface MyPostService {

    Page<Post> getMyPostListByStatus(Long memberId, Status status, Integer page);

    MyPostDTO myPostDTO(Post post);

    MyPostListDTO myPostListDTO(Page<Post> postList);
}
