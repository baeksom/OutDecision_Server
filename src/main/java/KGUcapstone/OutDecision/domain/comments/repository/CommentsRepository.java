package KGUcapstone.OutDecision.domain.comments.repository;

import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
    // 게시글 댓글목록
    List<Comments> getCommentsByPostOrderById(Post post);
    Optional<Comments> findByPostIdAndId(Long postId, Long id);
}
