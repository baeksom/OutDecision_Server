    package KGUcapstone.OutDecision.domain.vote.util;

    import KGUcapstone.OutDecision.domain.post.domain.Post;
    import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
    import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
    import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
    import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
    import KGUcapstone.OutDecision.domain.vote.service.MailService;
    import org.springframework.scheduling.annotation.Scheduled;
    import org.springframework.stereotype.Component;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.LocalDateTime;
    import java.time.ZoneId;
    import java.util.Date;
    import java.util.List;

    @Component
    public class VoteClosingTask {

        private final PostRepository postRepository;
        private final VoteRepository voteRepository;
        private final MemberRepository memberRepository;
        private final MailService mailService;

        public VoteClosingTask(PostRepository postRepository, VoteRepository voteRepository, MemberRepository memberRepository, MailService mailService) {
            this.postRepository = postRepository;
            this.voteRepository = voteRepository;
            this.memberRepository = memberRepository;
            this.mailService = mailService;
        }

//        @Scheduled(cron = "0 0 3 ? * SAT") // 테스트용 매주 토요일 2:54분에 실행
        // 매 시간의 00분, 10분, 20분, 30분, 40분, 50분에 실행되도록 스케줄링
        @Scheduled(cron = "0 0/10 * * * *")
        @Transactional
        public void closeVotes() {
            // 현재 시간
            LocalDateTime now = LocalDateTime.now();

            // Date 형식의 현재 시간
            Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());

            // 현재 시간 이전에 마감시간이 지난 투표 게시물들을 가져옵니다.
            List<Post> postsToClose = postRepository.findByStatusAndDeadlineBefore(Status.VOTING, currentDate);

            // 투표 상태를 CLOSING으로 변경합니다.
            for (Post post : postsToClose) {
                post.setStatus(Status.CLOSING);
                // 변경된 상태를 데이터베이스에 저장합니다.
                postRepository.save(post);
//                Long postId = post.getId();
                String title = post.getTitle();
                String email = post.getMember().getEmail();
                mailService.sendNotification(email, title);
//                List<Long> votedMemberIds = voteRepository.findMemberIdsByPostId(postId);
//                for (Long memberId:votedMemberIds) {
//                    String email = memberRepository.findById(memberId).get().getEmail();
//                    mailService.sendNotification(email, title);
//                }
            }
        }
    }
