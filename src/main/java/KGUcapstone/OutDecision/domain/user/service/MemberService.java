package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(memberRepository.findByEmail(email));
    }

    public void registerMember(String email, String provider, String nickname, String userImg) {
        // 사용자 정보를 이용하여 User 객체 생성
        Member newMember = Member.builder()
                .email(email)
                .nickname(nickname)
                .userRole("ROLE_USER")
                .socialType(provider)
                .bumps(0)
                .point(0)
                .userImg(userImg)
                .build();

        // UserRepository를 통해 새로운 사용자를 데이터베이스에 저장
        memberRepository.save(newMember);
    }

}
