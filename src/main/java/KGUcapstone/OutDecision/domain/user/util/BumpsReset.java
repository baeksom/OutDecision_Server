package KGUcapstone.OutDecision.domain.user.util;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BumpsReset {
    private final MemberRepository memberRepository;

    @Autowired
    public BumpsReset(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

//    @Scheduled(cron = "0 6 17 ? * Fri") // 테스트용 매주 금요일 17:6분에 실행
    @Scheduled(cron = "0 0 0 * * *")    // 매일 00시 정각
    public void bumpsReset() {
        List<Member> members = memberRepository.findAll();
        int bumps = 3;
        for (Member member:members) {
            if (member.getBumps() != 3) {
                member.updateBumps(bumps);
                memberRepository.save(member);
            }
        }
    }
}
