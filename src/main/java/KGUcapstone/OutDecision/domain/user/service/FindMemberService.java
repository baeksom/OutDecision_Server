package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.global.security.dto.SecurityUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class FindMemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(memberRepository.findByEmail(email));
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    // 로그인한 사용자 id 찾기
    public Long findLoginMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("authentication = " + authentication);
//        System.out.println("Principal 객체: " + authentication.getPrincipal());
//        System.out.println("Principal 객체 타입: " + authentication.getPrincipal().getClass().getName());

        if (authentication.getPrincipal() instanceof SecurityUserDto securityUserDto) {
            String email = securityUserDto.getEmail();
            Member member = memberRepository.findByEmail(email);
            Long memberId = member.getId();
            return memberId;
        }
        else return 0L;
    }
}
