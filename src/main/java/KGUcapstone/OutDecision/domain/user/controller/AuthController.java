package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.service.TokenService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final TokenService tokenService;

    @PostMapping("/token/logout")
    public ApiResponse<Object> logout(@RequestHeader("Authorization") final String accessToken) {
        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(accessToken);
        return ApiResponse.onSuccess(null);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<ApiResponse<Object>> refresh(@RequestHeader("Authorization") final String accessToken) {
        String newAccessToken = tokenService.republishAccessToken(accessToken);
        if (StringUtils.hasText(newAccessToken)) {
            return ResponseEntity.ok(ApiResponse.onSuccess(newAccessToken));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.onFailure("TOKEN400", "Access Token 발급에 실패했습니다.", null));
    }

    // 토큰 만료를 확인하기 위한 테스트 컨트롤러
    @PostMapping("/test")
    public ApiResponse<Object> test() {
        return ApiResponse.onSuccess(null);
    }

}