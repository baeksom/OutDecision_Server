package KGUcapstone.OutDecision.domain.user.auth.service;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.service.MemberService;
import KGUcapstone.OutDecision.global.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RevokeService {

    private final MemberService memberService;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;

    public void deleteAccount (String accessToken) throws IOException {
        String accessTokenWithoutBearer = accessToken.replace("Bearer ", "");
        // AccessToken 내부의 payload에 있는 email로 user를 조회한다.
        Optional<Member> findMember = memberService.findByEmail(jwtUtil.getUid(accessTokenWithoutBearer));
        if (findMember.isEmpty()) {
            log.info("회원을 찾을 수 없습니다.");
            throw new BadRequestException();
        }
        Member member = findMember.get();

        // 회원정보 삭제
        memberService.deleteMember(member);
        // 토큰 삭제
        tokenService.removeRefreshToken(accessTokenWithoutBearer);
    }
}
