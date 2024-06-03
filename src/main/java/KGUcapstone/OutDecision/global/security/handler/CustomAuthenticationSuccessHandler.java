package KGUcapstone.OutDecision.global.security.handler;

import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import KGUcapstone.OutDecision.global.security.dto.GeneratedToken;
import KGUcapstone.OutDecision.global.common.util.AESUtil;
import KGUcapstone.OutDecision.global.common.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${JOIN_SECRET}")
    String joinSecret;

    @Value("${IP}")
    private String ip;

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

        log.info("User authenticated with email: {}", email);
        log.info("Provider: {}", provider);
        log.info("User exists: {}", isExist);
        log.info("Role: {}", role);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        // 회원이 존재할경우
        if (isExist) {
            // 회원이 존재하면 jwt token 발행을 시작한다.
            GeneratedToken token = jwtUtil.generateToken(email, role);
            log.info("jwtToken = {}", token.getAccessToken());
            // 쿠키로 accessToken 전달
            addCookie(response, "Authorization", token.getAccessToken(), 60*60);
            // 로그인 확인 페이지로 리다이렉트 시킨다.
            log.info("Redirecting to {}", ip + "/");
            getRedirectStrategy().sendRedirect(request, response, ip+"/");

            // JSON 응답 생성
            objectMapper.writeValue(response.getWriter(), ApiResponse.onSuccess(null));
        }
        else {
            log.info("소셜 회원가입 redirect 준비");

            // join_token 생성
            String join_token = AESUtil.encrypt(joinSecret, email+"&&"+provider);
            System.out.println("join_token = " + join_token);

            addCookie(response, "email", email, 60*5);
            addCookie(response, "provider", provider, 60*5);  // 5분
            log.info("Redirecting to {}", ip + "/signup/social?join_token=" + join_token);
            getRedirectStrategy().sendRedirect(request, response, ip+"/signup/social?join_token="+join_token);
            objectMapper.writeValue(response.getWriter(), ApiResponse.onFailure("401", "회원가입 필요", join_token));
        }
    }
}