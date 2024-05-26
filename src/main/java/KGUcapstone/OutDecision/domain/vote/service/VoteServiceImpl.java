package KGUcapstone.OutDecision.domain.vote.service;

import KGUcapstone.OutDecision.domain.options.domain.Options;
import KGUcapstone.OutDecision.domain.options.repository.OptionsRepository;
import KGUcapstone.OutDecision.domain.post.service.PostService;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.user.service.mypage.TitleService;
import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteServiceImpl implements VoteService{

    private final VoteRepository voteRepository;
    private final FindMemberService findMemberService;
    private final OptionsRepository optionsRepository;
    private final TitleService titleService;
    private final PostService postService;

    @Override
    public boolean addVote(List<Long> optionsIds) {
        Optional<Member> member = findMemberService.findLoginMember();
        boolean isPointed = false;

        for (Long id : optionsIds) {
            Optional<Options> options = optionsRepository.findById(id);

            if (member.isPresent() && options.isPresent()) {
                // 사용자가 존재하고, 옵션이 있으면 - 투표
                // 포인트 +10
                if(!isPointed) {
                    member.get().updatePoint(member.get().getPoint() + 10);
                    isPointed = true;
                }

                // 투표 저장
                Vote vote = Vote.builder()
                        .member(member.get())
                        .options(options.get())
                        .build();
                voteRepository.save(vote);

                // 핫 게시글 가능 여부 확인
                postService.turnsHot(options.get().getPost());

                // 칭호 획득 가능 여부 확인
                titleService.memberGetTitle(options.get().getPost(), member.get());

                System.out.println("-- 투표 완료 --");
            } else if (member.isPresent()) {
                System.out.println("옵션이 없음");
                return false;
            } else if (options.isPresent()) {
                System.out.println("멤버가 없음");
                return false;
            }
            else {
                // 예외 처리
                return false;
            }
        }
        return true;
    }
}
