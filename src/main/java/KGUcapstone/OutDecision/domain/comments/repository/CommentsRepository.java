package KGUcapstone.OutDecision.domain.comments.repository;

import KGUcapstone.OutDecision.domain.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CommentsRepository extends JpaRepository<Likes, Long> {
}
