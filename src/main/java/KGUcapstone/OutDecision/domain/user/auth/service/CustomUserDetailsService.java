package KGUcapstone.OutDecision.domain.user.auth.service;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.auth.dto.CustomUserDetails;
import KGUcapstone.OutDecision.domain.user.auth.dto.RegisterRequestDto;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final FindMemberService findMemberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 사용자를 데이터베이스 등에서 찾는 코드
        Optional<Member> member = findMemberService.findByEmail(username);

        // 사용자가 없는 경우 예외 처리
        if (member.isPresent()) {
            return new CustomUserDetails(member.get());
        }
        throw new UsernameNotFoundException("User not found with email: " + username);
    }

    public void saveMember(RegisterRequestDto request){
        Member member = Member.builder()
                .name(request.getName())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .userImg(request.getUserImg())
                .userRole("USER")
                .point(0)
                .bumps(0)
                .build();

        memberRepository.save(member);
    }


}
