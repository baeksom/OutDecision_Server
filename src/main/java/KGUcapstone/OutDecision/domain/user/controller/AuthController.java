package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.StatusResponseDto;
import KGUcapstone.OutDecision.domain.user.dto.TokenResponseStatus;
import KGUcapstone.OutDecision.domain.user.repository.RefreshTokenRepository;
import KGUcapstone.OutDecision.domain.user.service.JwtUtil;
import KGUcapstone.OutDecision.domain.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenRepository tokenRepository;
    private final RefreshTokenService tokenService;
    private final JwtUtil jwtUtil;

    @PostMapping("/token/logout")
    public ResponseEntity<StatusResponseDto> logout(@RequestHeader("Authorization") final String accessToken) {

        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(accessToken);
        return ResponseEntity.ok(StatusResponseDto.addStatus(200));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponseStatus> refresh(@RequestHeader("Authorization") final String accessToken) {

        String newAccessToken = tokenService.republishAccessToken(accessToken);
        if (StringUtils.hasText(newAccessToken)) {
            return ResponseEntity.ok(TokenResponseStatus.addStatus(200, newAccessToken));
        }

        return ResponseEntity.badRequest().body(TokenResponseStatus.addStatus(400, null));
    }

}