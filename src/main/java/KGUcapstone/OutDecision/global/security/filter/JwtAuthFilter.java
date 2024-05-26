package KGUcapstone.OutDecision.global.security.filter;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.user.service.auth.TokenService;
import KGUcapstone.OutDecision.global.security.dto.SecurityUserDto;
import KGUcapstone.OutDecision.global.common.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static KGUcapstone.OutDecision.global.common.util.CookieUtil.addCookie;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final FindMemberService findMemberService;
    private final TokenService tokenService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().contains("/token/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtAuthFilter is called for request URI: {}", request.getRequestURI());
        String atc = findMemberService.getTokenFromCookies();

        if (!StringUtils.hasText(atc)) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isTokenValid = jwtUtil.verifyToken(atc);

        if (!isTokenValid) {
            log.info("토큰 만료 -> 재발급");
            String newAccessToken = tokenService.republishAccessToken(atc, response);

            if (newAccessToken != null) {
                addCookie(response, "Authorization", newAccessToken, 60 * 60);
                atc = newAccessToken;
                log.info("토큰 발급 완료 필터 newAccessToken = {}", newAccessToken);
                return;
            } else {
                log.error("새로운 토큰 발급 실패");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "토큰 재발급 실패");
                return;
            }
        }

        // 아래 코드는 AccessToken이 유효할 때만 실행됨
        if (jwtUtil.verifyToken(atc)) {
            Member findMember = findMemberService.findByEmail(jwtUtil.getUid(atc)).orElse(null);

            if (findMember != null) {
                SecurityUserDto userDto = SecurityUserDto.builder()
                        .memberId(findMember.getId())
                        .email(findMember.getEmail())
                        .role("ROLE_".concat(findMember.getUserRole()))
                        .nickname(findMember.getNickname())
                        .build();

                Authentication auth = getAuthentication(userDto);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        filterChain.doFilter(request, response);
    }

    public Authentication getAuthentication(SecurityUserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                List.of(new SimpleGrantedAuthority(member.getRole())));
    }
}
