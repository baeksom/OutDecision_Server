package KGUcapstone.OutDecision.domain.user.service.auth;

import KGUcapstone.OutDecision.domain.user.dto.RefreshToken;
import KGUcapstone.OutDecision.domain.user.repository.TokenRepository;
import KGUcapstone.OutDecision.global.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static KGUcapstone.OutDecision.global.common.util.CookieUtil.addCookie;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {

    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;

    // Redis에서 토큰 삭제
    @Transactional
    public void removeRefreshToken(String accessToken) {
        System.out.println("accessToken = " + accessToken);

        // 헤더에서 가져온 값 앞에 자동 생성되는 "Bearer "을 삭제한다.
        String cleanedToken = accessToken.replace("Bearer ", "");
        log.info("Attempting to remove refreshToken for accessToken: {}", accessToken.replace("Bearer ", ""));

        RefreshToken token = tokenRepository.findByAccessToken(cleanedToken)
                .orElseThrow(() -> {
                    log.error("RefreshToken not found for accessToken: {}", cleanedToken);
                    return new IllegalArgumentException("RefreshToken not found");
                });
        tokenRepository.delete(token);
    }

    // AccessToken 재생성
    @Transactional
    public String republishAccessToken(String accessToken, HttpServletResponse response) {
        // 액세스 토큰으로 Refresh 토큰 객체를 조회
        Optional<RefreshToken> refreshToken = tokenRepository.findByAccessToken(accessToken.replace("Bearer ", ""));

        // RefreshToken이 존재하고 유효하다면 실행
        if (refreshToken.isPresent() && jwtUtil.verifyToken(refreshToken.get().getRefreshToken())) {
            // RefreshToken 객체를 꺼내온다.
            RefreshToken resultToken = refreshToken.get();
            // 권한과 아이디를 추출해 새로운 액세스토큰을 만든다.
            String newAccessToken = jwtUtil.generateAccessToken(resultToken.getId(), jwtUtil.getRole(resultToken.getRefreshToken()));
            // 액세스 토큰의 값을 수정해준다.
            resultToken.updateAccessToken(newAccessToken);
            tokenRepository.save(resultToken);
            log.info("Attempting to republish accessToken: {}", newAccessToken);
            // 새로운 액세스 토큰을 반환해준다.
            return newAccessToken;
        }

        return null;
    }
}