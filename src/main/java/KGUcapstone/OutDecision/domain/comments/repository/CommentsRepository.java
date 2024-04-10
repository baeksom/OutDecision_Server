package KGUcapstone.OutDecision.domain.comments.repository;

import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comments, Long> {
}
