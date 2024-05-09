package KGUcapstone.OutDecision.domain.vote.service;

import KGUcapstone.OutDecision.domain.options.domain.Options;
import KGUcapstone.OutDecision.domain.options.repository.OptionsRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService{

    private final VoteRepository voteRepository;
    private final FindMemberService findMemberService;
    private final OptionsRepository optionsRepository;

    public boolean addVote(Long optionsId) {
        Optional<Member> member = findMemberService.findLoginMember();
        Optional<Options> options = optionsRepository.findById(optionsId);
        if (member.isPresent() && options.isPresent()) {
            Vote vote = Vote.builder()
                    .member(member.get())
                    .options(options.get())
                    .build();

            voteRepository.save(vote);
            System.out.println("-- 투표 완료 --");
            return true;
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

}
