package KGUcapstone.OutDecision.domain.user.handler;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.CustomUser;
import KGUcapstone.OutDecision.domain.user.dto.GeneratedToken;
import KGUcapstone.OutDecision.domain.user.service.TokenSaveService;
import KGUcapstone.OutDecision.global.util.JwtConstants;
import KGUcapstone.OutDecision.global.util.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final TokenSaveService tokenSaveService;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 인증 성공 후 처리되는 핸들러이기 때문에 전달받은 authentication 은 인증이 완료된 객체
        Member member = ((CustomUser) authentication.getPrincipal()).getMember();
        String email = member.getEmail();
        String role = member.getUserRole();

        // jwt token 발행을 시작한다.
        GeneratedToken token = jwtUtil.generateToken(email, role);
        log.info("jwtToken = {}", token.getAccessToken());

        // 헤더로 accessToken 전달
        response.addHeader(JwtConstants.JWT_HEADER, JwtConstants.JWT_TYPE + token.getAccessToken());
        // Refresh Token 은 Cookie 에 담아서 전달하되, XSS 공격 방어를 위해 HttpOnly 를 설정한다
        Cookie cookie = new Cookie(JwtConstants.REFRESH, token.getRefreshToken());
        cookie.setMaxAge(60*5);     // 5분 설정
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        log.info("일반 로그인 redirect 준비");
        log.info("ResponseHeader Authorization : " + response.getHeader(JwtConstants.JWT_HEADER));
        // 로그인 확인 페이지로 리다이렉트 시킨다.
        getRedirectStrategy().sendRedirect(request, response, "/loginSuccess");
    }
}