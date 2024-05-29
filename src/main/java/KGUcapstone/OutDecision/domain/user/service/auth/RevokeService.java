package KGUcapstone.OutDecision.domain.user.service.auth;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.user.service.S3Service;
import KGUcapstone.OutDecision.global.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RevokeService {

    private final FindMemberService findMemberService;
    private final TokenService tokenService;
    private final JwtUtil jwtUtil;
    private final S3Service s3Service;

    public void deleteAccount (String accessToken, HttpServletResponse response) throws IOException {
        String accessTokenWithoutBearer = accessToken.replace("Bearer ", "");
        // AccessToken 내부의 payload에 있는 email로 user를 조회한다.
        Optional<Member> findMember = findMemberService.findByEmail(jwtUtil.getUid(accessTokenWithoutBearer));
        if (findMember.isEmpty()) {
            log.info("회원을 찾을 수 없습니다.");
            throw new BadRequestException();
        }
        Member member = findMember.get();


        // 회원정보 삭제
        findMemberService.deleteMember(member);
        // 토큰 삭제
        tokenService.removeRefreshToken(accessTokenWithoutBearer, response);
        // ncloud 프로필사진 삭제
        s3Service.deleteImage(member.getUserImg());
        System.out.println("-- 회원 삭제 완료 --");
    }
}
