package KGUcapstone.OutDecision.global.security.handler;

import KGUcapstone.OutDecision.global.security.dto.GeneratedToken;
import KGUcapstone.OutDecision.global.common.util.AESUtil;
import KGUcapstone.OutDecision.global.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static KGUcapstone.OutDecision.global.common.util.CookieUtil.addCookie;


@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Value("${JOIN_SECRET}")
    String joinSecret;

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        // OAuth2User로 캐스팅하여 인증된 사용자 정보를 가져온다.
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // 사용자 이메일을 가져온다.
        String email = oAuth2User.getAttribute("email");
        // 서비스 제공 플랫폼(GOOGLE, KAKAO, NAVER)이 어디인지 가져온다.
        String provider = oAuth2User.getAttribute("provider");

        // CustomOAuth2UserService에서 셋팅한 로그인한 회원 존재 여부를 가져온다.
        boolean isExist = oAuth2User.getAttribute("exist");
        // OAuth2User로 부터 Role을 얻어온다.
        String role = oAuth2User.getAuthorities().stream()
                .findFirst() // 첫번째 Role을 찾아온다.
                .orElseThrow(IllegalAccessError::new) // 존재하지 않을 시 예외를 던진다.
                .getAuthority(); // Role을 가져온다.

        // 회원이 존재할경우
        if (isExist) {
            // 회원이 존재하면 jwt token 발행을 시작한다.
            GeneratedToken token = jwtUtil.generateToken(email, role);
            log.info("jwtToken = {}", token.getAccessToken());

            // 쿠키로 accessToken 전달
            addCookie(response, "Authorization", token.getAccessToken(), 60*60);

            // 로그인 확인 페이지로 리다이렉트 시킨다.
            log.info("소셜 로그인 redirect 준비");
            getRedirectStrategy().sendRedirect(request, response, "/loginSuccess");
        }
        else {
            log.info("소셜 회원가입 redirect 준비");

            // join_token 생성
            String join_token = AESUtil.encrypt(joinSecret, email+provider);
            System.out.println("join_token = " + join_token);

            addCookie(response, "email", email, 60*5);
            addCookie(response, "provider", provider, 60*5);  // 5분

            getRedirectStrategy().sendRedirect(request, response, "/register/v1?join_token="+join_token);
        }
    }
}