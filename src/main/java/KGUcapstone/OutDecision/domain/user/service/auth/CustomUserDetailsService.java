package KGUcapstone.OutDecision.domain.user.service.auth;

import KGUcapstone.OutDecision.domain.title.domain.Missions;
import KGUcapstone.OutDecision.domain.title.domain.Title;
import KGUcapstone.OutDecision.domain.title.repository.MissionsRepository;
import KGUcapstone.OutDecision.domain.title.repository.TitleRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.CustomUserDetails;
import KGUcapstone.OutDecision.domain.user.dto.RegisterRequestDto;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.user.service.S3Service;
import KGUcapstone.OutDecision.global.common.util.AESUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    private final FindMemberService findMemberService;
    private final MemberRepository memberRepository;
    private final TitleRepository titleRepository;
    private final MissionsRepository missionsRepository;
    private final S3Service s3Service;

    @Value("${DEFAULT_PROFILE_IMG}")
    private String defaultImg;

    @Value("${JOIN_SECRET}")
    String joinSecret;

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

    public void saveMember(String email, String provide, String nickname, MultipartFile userImg) {
        String profileImage = "";
        if (userImg.isEmpty()) profileImage = defaultImg;
        else profileImage = s3Service.uploadFile(userImg, "profile");

        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .socialType(provide)
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

        Missions missions = Missions.builder()
                .member(member)
                .fashionista_cnt(0)
                .foodie_cnt(0)
                .traveler_cnt(0)
                .ceo_cnt(0)
                .romantist_cnt(0)
                .hobbyist_cnt(0)
                .greedy_cnt(0)
                .build();
        missionsRepository.save(missions);
    }


}