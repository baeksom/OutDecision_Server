package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.LikedPostDTO;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.MyPostDTO;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.MyPostListDTO;
import org.springframework.data.domain.Page;


public interface MyActivityService {

    Page<Post> getMyPostListByStatus(Long memberId, Status status, Integer page);

    Page<Post> getLikedPostListByStatus(Long memberId, Status status, Integer page);

    MyPostDTO myPost(Post post);

    MyPostListDTO myPostList(Page<Post> postList);

    LikedPostDTO likedPost(Post post);

    // 좋아요한 글 list
    ActivityResponseDTO.LikedPostListDTO likedPostList(Page<Post> postList);
}
