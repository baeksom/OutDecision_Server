package KGUcapstone.OutDecision.domain.notifications.repository;

import KGUcapstone.OutDecision.domain.notifications.domain.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    @Query("SELECT member.id FROM Notifications WHERE post.id = :postId")
    List<Long> findMemberIdsByPostId(Long postId);
    Notifications findByMemberIdAndPostId(Long memberId, Long postId);
}
