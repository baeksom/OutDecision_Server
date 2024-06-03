package KGUcapstone.OutDecision.domain.notifications.service;

import KGUcapstone.OutDecision.domain.notifications.domain.Notifications;
import KGUcapstone.OutDecision.domain.notifications.repository.NotificationsRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.global.error.exception.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.exception.handler.NotificationHandler;
import KGUcapstone.OutDecision.global.error.exception.handler.PostHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService{
    private final NotificationsRepository notificationsRepository;
    private final PostRepository postRepository;
    private final FindMemberService findMemberService;

    @Override
    public boolean addNotifications(Long postId) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Member member;
        // 로그인 체크
        if(memberOptional.isPresent()) member = memberOptional.get();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        Notifications notifications = Notifications.builder()
                .post(post)
                .member(member)
                .build();
        notificationsRepository.save(notifications);
        return true;
    }

    @Override
    public boolean deleteNotifications(Long postId) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Long memberId;
        // 로그인 체크
        if(memberOptional.isPresent()) memberId = memberOptional.get().getId();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        Post post = postRepository.findById(postId).orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        Notifications notifications = notificationsRepository.findByMemberIdAndPostId(memberId, postId);
        if (notifications == null) {
            throw new NotificationHandler(ErrorStatus.NOTIFICATION_NOT_FOUND);
        }
        notificationsRepository.delete(notifications);
        return true;
    }
}

