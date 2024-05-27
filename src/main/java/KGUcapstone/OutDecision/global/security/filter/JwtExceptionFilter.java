package KGUcapstone.OutDecision.global.security.filter;

import KGUcapstone.OutDecision.domain.user.service.auth.TokenService;
import KGUcapstone.OutDecision.global.common.util.CookieUtil;
import KGUcapstone.OutDecision.global.common.util.JwtUtil;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static KGUcapstone.OutDecision.global.common.util.CookieUtil.deleteCookie;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtExceptionFilter is called for request URI: {}", request.getRequestURI());

        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.error("JWT Exception caught: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), ApiResponse.onFailure("401",e.getMessage(),null));
            deleteCookie(response, "Authorization");
        }
    }
}
