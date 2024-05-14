package KGUcapstone.OutDecision.domain.notifications.repository;

import KGUcapstone.OutDecision.domain.notifications.domain.Notifications;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {

    Optional<Notifications> findByPostAndMember(Post post, Member member);
}
