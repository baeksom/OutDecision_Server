package KGUcapstone.OutDecision.domain.notifications.repository;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import KGUcapstone.OutDecision.domain.notifications.domain.Notifications;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    Optional<Notifications> findByPostAndMember(Post post, Member member);
    @Query("SELECT member.id FROM Notifications WHERE post.id = :postId")
    List<Long> findMemberIdsByPostId(Long postId);
    Notifications findByMemberIdAndPostId(Long memberId, Long postId);
    @Query("SELECT COUNT(n) > 0 FROM Notifications n WHERE n.member.id = :memberId AND n.post.id = :postId")
    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}
