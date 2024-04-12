package KGUcapstone.OutDecision.domain.likes.repository;

import KGUcapstone.OutDecision.domain.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Query("SELECT post.id FROM Likes WHERE member.id = :memberId")
    List<Long> findPostIdsByMemberId(Long memberId);
}