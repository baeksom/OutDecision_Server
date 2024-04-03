package KGUcapstone.OutDecision.domain.user.handler;

import KGUcapstone.OutDecision.domain.user.dto.GeneratedToken;
import KGUcapstone.OutDecision.global.util.CookieUtil;
import KGUcapstone.OutDecision.global.util.JwtConstants;
import KGUcapstone.OutDecision.global.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

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

//            // accessToken을 쿼리스트링에 담는 url을 만들어준다.
//            String targetUrl = UriComponentsBuilder.fromUriString("/loginSuccess")
//                    .queryParam("accessToken", token.getAccessToken())
//                    .build()
//                    .encode(StandardCharsets.UTF_8)
//                    .toUriString();

            // 헤더로 accessToken 전달
            response.addHeader(JwtConstants.JWT_HEADER, JwtConstants.JWT_TYPE + token.getAccessToken());
            // Refresh Token 은 Cookie 에 담아서 전달하되, XSS 공격 방어를 위해 HttpOnly 를 설정한다
            Cookie cookie = new Cookie(JwtConstants.REFRESH, token.getRefreshToken());
            cookie.setMaxAge(60*5);     // 5분 설정
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            log.info("소셜 로그인 redirect 준비");
            log.info("ResponseHeader Authorization : " + response.getHeader(JwtConstants.JWT_HEADER));
            // 로그인 확인 페이지로 리다이렉트 시킨다.
            getRedirectStrategy().sendRedirect(request, response, "/loginSuccess");
        }
        else {
            cookieUtil.addCookie(response, "email", email, 60*5);
            cookieUtil.addCookie(response, "provider", provider, 60*5);  // 5분
            getRedirectStrategy().sendRedirect(request, response, "/user/register/v1");
        }
    }
}