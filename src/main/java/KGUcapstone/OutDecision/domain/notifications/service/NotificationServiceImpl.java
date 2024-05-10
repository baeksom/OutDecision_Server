package KGUcapstone.OutDecision.domain.notifications.service;

import KGUcapstone.OutDecision.domain.notifications.domain.Notifications;
import KGUcapstone.OutDecision.domain.notifications.repository.NotificationsRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService{
    private final NotificationsRepository notificationsRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    @Override
    public boolean addNotifications(Long postId) {
        Long memberId = 2024L;
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post를 찾을 수 없습니다."));
        Notifications notifications = Notifications.builder()
                .post(post)
                .member(member)
                .build();
        notificationsRepository.save(notifications);
        return true;
    }

    @Override
    public boolean deleteNotifications(Long postId) {
        Long memberId = 2024L;
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post를 찾을 수 없습니다."));
        Notifications notifications = notificationsRepository.findByMemberIdAndPostId(memberId, postId);
        notificationsRepository.delete(notifications);
        return true;
    }
}
