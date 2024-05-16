package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import org.springframework.data.domain.Page;


public interface MyActivityService{
    Page<Post> getMyPostListByStatus(Status status, Integer page);
    Page<Post> getLikedPostListByStatus(Status status, Integer page);
    Page<Post> getVotedPostListByStatus(Status status, Integer page);
}
