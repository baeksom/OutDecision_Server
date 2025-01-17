package KGUcapstone.OutDecision.domain.user.service.auth;

import KGUcapstone.OutDecision.domain.title.domain.Missions;
import KGUcapstone.OutDecision.domain.title.domain.Title;
import KGUcapstone.OutDecision.domain.title.repository.MissionsRepository;
import KGUcapstone.OutDecision.domain.title.repository.TitleRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.user.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

// 사용자 정보 가져오기
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final FindMemberService findMemberService;
    private final MemberRepository memberRepository;
    private final TitleRepository titleRepository;
    private final MissionsRepository missionsRepository;
    private final S3Service s3Service;

    @Value("${DEFAULT_PROFILE_IMG}")
    private String defaultImg;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService 객체 생성
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();

        // OAuth2UserService를 사용하여 OAuth2User 정보를 가져온다.
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 클라이언트 등록 ID(google, naver, kakao)와 사용자 이름 속성을 가져온다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();


        // OAuth2UserService를 사용하여 가져온 OAuth2User 정보로 OAuth2Attribute 객체를 만든다.
        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        // OAuth2Attribute의 속성값들을 Map으로 반환 받는다.
        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();

        // 사용자 email(또는 id) 정보를 가져온다.
        String email = (String) memberAttribute.get("email");
        // 이메일로 가입된 회원인지 조회한다.
        Optional<Member> findMember = findMemberService.findByEmail(email);

        if (findMember.isEmpty()) {
            // 회원이 존재하지 않을경우, memberAttribute의 exist 값을 false로 넣어준다.
            memberAttribute.put("exist", false);
            // 회원의 권한(회원이 존재하지 않으므로 기본권한인 ROLE_USER를 넣어준다), 회원속성, 속성이름을 이용해 DefaultOAuth2User 객체를 생성해 반환한다.
            return new DefaultOAuth2User(
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                    memberAttribute, "email");
        }
        else if (!registrationId.equals(findMember.get().getSocialType())) {
            // 회원이 존재하지만, 같은 아이디의 다른 소셜타입인 경우
            log.info("이미 다른 소셜로 가입된 이메일입니다.");
            throw new OAuth2AuthenticationException(new OAuth2Error("registration_failure", "Trying to register with different social type for same email.", null));
        }

        // 회원이 존재할경우, memberAttribute의 exist 값을 true로 넣어준다.
        memberAttribute.put("exist", true);
        // 회원의 권한과, 회원속성, 속성이름을 이용해 DefaultOAuth2User 객체를 생성해 반환한다.
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_".concat(findMember.get().getUserRole()))),
                memberAttribute, "email");

    }

    public void registerSocialMember(String email, String provider, String nickname, MultipartFile userImg) {
        String profileImage = "";
        if (userImg.isEmpty()) profileImage = defaultImg;
        else profileImage = s3Service.uploadFile(userImg, "profile");

        // 사용자 정보를 이용하여 User 객체 생성
        Member newMember = Member.builder()
                .email(email)
                .nickname(nickname)
                .userRole("USER")
                .socialType(provider)
                .bumps(3)
                .point(0)
                .userImg(profileImage)
                .build();

        // UserRepository를 통해 새로운 사용자를 데이터베이스에 저장
        memberRepository.save(newMember);

        // 칭호 튜플 생성 후 member 연결
        Title title = Title.builder()
                .member(newMember)
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
                .member(newMember)
                .ceo_cnt(0)
                .fashionista_cnt(0)
                .foodie_cnt(0)
                .hobbyist_cnt(0)
                .romantist_cnt(0)
                .traveler_cnt(0)
                .greedy_cnt(0)
                .build();
        missionsRepository.save(missions);

    }
}