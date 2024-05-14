package KGUcapstone.OutDecision.domain.notifications.repository;

import KGUcapstone.OutDecision.domain.notifications.domain.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}
