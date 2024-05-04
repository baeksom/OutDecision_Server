package KGUcapstone.OutDecision.domain.user.service.auth;

import KGUcapstone.OutDecision.domain.title.domain.Title;
import KGUcapstone.OutDecision.domain.title.repository.TitleRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.CustomUserDetails;
import KGUcapstone.OutDecision.domain.user.dto.RegisterRequestDto;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.user.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final FindMemberService findMemberService;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TitleRepository titleRepository;
    private final S3Service s3Service;

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

    public void saveMember(RegisterRequestDto request, MultipartFile userImg){
        String profileImage = "";
        if (userImg.isEmpty()) profileImage = "https://kr.object.ncloudstorage.com/outdecisionbucket/profile/3b0ae8ae-78b6-4a05-86fc-070900b8b763.png";
        else profileImage = s3Service.uploadFile(userImg, "profile");

        Member member = Member.builder()
                .name(request.getName())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userImg(profileImage)
                .userRole("USER")
                .point(0)
                .bumps(3)
                .build();
        memberRepository.save(member);

        Title title = Title.builder()
                .member(member)
                .ceo(false)
                .fashionista(false)
                .foodie(false)
                .hobbyist(false)
                .romantist(false)
                .sprout(true)
                .traveler(false)
                .greedy(false)
                .first(false)
                .second(false)
                .third(false)
                .build();
        titleRepository.save(title);

    }


}
