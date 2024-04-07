package KGUcapstone.OutDecision.domain.user.security.handler;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.security.dto.CustomUserDetails;
import KGUcapstone.OutDecision.domain.user.security.dto.GeneratedToken;
import KGUcapstone.OutDecision.global.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static KGUcapstone.OutDecision.global.util.CookieUtil.addCookie;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 인증 성공 후 처리되는 핸들러이기 때문에 전달받은 authentication 은 인증이 완료된 객체
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();
        String email = member.getEmail();
        String role = member.getUserRole();

        // jwt token 발행을 시작한다.
        GeneratedToken token = jwtUtil.generateToken(email, role);
        log.info("jwtToken = {}", token.getAccessToken());

        // 쿠키로 accessToken 전달
        addCookie(response, "accessToken", token.getAccessToken(), 60*5);

        // 로그인 확인 페이지로 리다이렉트 시킨다.
        log.info("일반 로그인 redirect 준비");
        getRedirectStrategy().sendRedirect(request, response, "/loginSuccess");
    }
}