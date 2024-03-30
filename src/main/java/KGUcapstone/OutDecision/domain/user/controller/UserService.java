package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final MemberRepository memberRepository;

    @Autowired
    public UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void registerUser(String email, String provider, String name, String phoneNumber) {
        // 사용자 정보를 이용하여 User 객체 생성
        Member newUser = Member.builder()
                .email(email)
                .name(name)
                .phone(phoneNumber)
                .nickname("임시값")
                .password("1234")
                .bumps(0)
                .point(0)
                .userImg("임시 저장값")
                .userRole("ROLE_USER")
                .build();

        // UserRepository를 통해 새로운 사용자를 데이터베이스에 저장
        memberRepository.save(newUser);
    }
}
