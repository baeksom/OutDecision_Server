package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import org.springframework.data.domain.Page;


public interface MyActivityService{
    Page<Post> getMyPostListByStatus(Long memberId, Status status, Integer page);
    Page<Post> getLikedPostListByStatus(Long memberId, Status status, Integer page);
    Page<Post> getVotedPostListByStatus(Long memberId, Status status, Integer page);
}
