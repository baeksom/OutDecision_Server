package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.user.service.auth.TokenService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static KGUcapstone.OutDecision.global.common.util.CookieUtil.addCookie;
import static KGUcapstone.OutDecision.global.common.util.CookieUtil.deleteCookie;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TokenController {

    private final TokenService tokenService;
    private final FindMemberService findMemberService;

    @PostMapping("/token/logout")
    public ApiResponse<Object> logout(HttpServletResponse response) {
        String accessToken = findMemberService.getTokenFromCookies();
        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(accessToken, response);
        // 쿠키에서 토큰 삭제
        deleteCookie(response, "Authorization");
        return ApiResponse.onSuccess(null);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponse<Object>> refresh(HttpServletResponse response) {
        String accessToken = findMemberService.getTokenFromCookies();
        String newAccessToken = tokenService.republishAccessToken(accessToken, response);
        if (StringUtils.hasText(newAccessToken)) {
            // 클라이언트에게 응답할 때 쿠키를 변경한다.
            return ResponseEntity.ok(ApiResponse.onSuccess(newAccessToken));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("TOKEN400", "Access Token 발급에 실패했습니다.", null));
    }
}