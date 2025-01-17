package KGUcapstone.OutDecision.global.common.util;

import KGUcapstone.OutDecision.global.security.dto.GeneratedToken;
import KGUcapstone.OutDecision.global.common.properties.JwtProperties;
import KGUcapstone.OutDecision.domain.user.service.auth.SaveTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperties jwtProperties;
    private final SaveTokenService accessTokenService;
    private String secretKey;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(jwtProperties.getSecret().getBytes());
    }


    public GeneratedToken generateToken(String email, String role) {
        // refreshToken과 accessToken을 생성한다.
        String refreshToken = generateRefreshToken(email, role);
        String accessToken = generateAccessToken(email, role);

        // 토큰을 Redis에 저장한다.
        accessTokenService.saveTokenInfo(email, refreshToken, accessToken);
        return new GeneratedToken(accessToken, refreshToken);
    }

    public String generateRefreshToken(String email, String role) {
        // 토큰의 유효 기간을 밀리초 단위로 설정.
        long refreshPeriod = 1000L * 60L * 60L * 24L * 14; // 2주

        // 새로운 클레임 객체를 생성하고, 이메일과 역할(권한)을 셋팅
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        // 현재 시간과 날짜를 가져온다.
        Date now = new Date();

        System.out.println("토큰 생성 - 이메일: " + email + ", 역할: " + role);

        return Jwts.builder()
                // Payload를 구성하는 속성들을 정의한다.
                .setClaims(claims)
                // 발행일자를 넣는다.
                .setIssuedAt(now)
                // 토큰의 만료일시를 설정한다.
                .setExpiration(new Date(now.getTime() + refreshPeriod))
                // 지정된 서명 알고리즘과 비밀 키를 사용하여 토큰을 서명한다.
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }


    public String generateAccessToken(String email, String role) {
        long tokenPeriod = 1000L * 60L * 60L; // 1시간

        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);

        Date now = new Date();
        return
                Jwts.builder()
                        // Payload를 구성하는 속성들을 정의한다.
                        .setClaims(claims)
                        // 발행일자를 넣는다.
                        .setIssuedAt(now)
                        // 토큰의 만료일시를 설정한다.
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        // 지정된 서명 알고리즘과 비밀 키를 사용하여 토큰을 서명한다.
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact();

    }


    public boolean verifyToken(String token) {
        token = token.replace("Bearer ", "");
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey) // 비밀키를 설정하여 파싱한다.
                    .parseClaimsJws(token);  // 주어진 토큰을 파싱하여 Claims 객체를 얻는다.

            // 로그: 토큰 파싱 성공
            System.out.println("토큰 파싱 성공");

            // 토큰의 만료 시간과 현재 시간 비교하여 유효성 검사 결과 반환
            boolean isValid = claims.getBody().getExpiration().after(new Date());

            // 로그: 유효성 검사 결과
            System.out.println("토큰 유효성 검사 결과: " + isValid);

            return isValid;
        } catch (Exception e) {
            // 로그: 예외 발생
            System.out.println("토큰 만료 확인 예외 처리 구간");
            System.out.println("token = " + token);

            // 예외 상세 정보 출력
            e.printStackTrace();

            return false;
        }
    }


    // 토큰에서 Email을 추출한다.
    public String getUid(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token.replace("Bearer ", "")).getBody().getSubject();
    }

    // 토큰에서 ROLE(권한)만 추출한다.
    public String getRole(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token.replace("Bearer ", "")).getBody().get("role", String.class);
    }

}