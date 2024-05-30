package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.title.repository.TitleRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.global.common.util.JwtUtil;
import KGUcapstone.OutDecision.global.error.exception.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindMemberService {

    private final MemberRepository memberRepository;
    private final TitleRepository titleRepository;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    public Optional<Member> findByEmail(String email) {
        return Optional.ofNullable(memberRepository.findByEmail(email));
    }

    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    public Long findLoginMemberId() {
        String token = getTokenFromCookies();
        if (token != null && jwtUtil.verifyToken(token)) {
            String email = jwtUtil.getUid(token);
            Member member = memberRepository.findByEmail(email);
            if (member != null) {
                return member.getId();
            }
        }
        return 0L;
    }

    public Optional<Member> findLoginMember() {
        String token = getTokenFromCookies();
        if (token != null && jwtUtil.verifyToken(token)) {
            String email = jwtUtil.getUid(token);
            Member member = memberRepository.findByEmail(email);
            return Optional.ofNullable(member);
        }
        return Optional.empty();
    }

    public String getTokenFromCookies() {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> "Authorization".equals(cookie.getName()))
                .map(cookie -> cookie.getValue().replace("Bearer ", ""))
                .findFirst()
                .orElse(null);
    }

    public MemberResponseDTO.LoginSuccessMemberDTO getLoginSuccessMember() {
        Optional<Member> memberOptional = findLoginMember();
        Member member;
        // 로그인 체크
        if(memberOptional.isPresent()) member = memberOptional.get();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        int titleCnt = titleRepository.countTrueColumnsForMember(member.getId());

        return MemberResponseDTO.LoginSuccessMemberDTO.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .userImg(member.getUserImg())
                .memberTitle(member.getUserTitle())
                .titleCnt(titleCnt)
                .point(member.getPoint())
                .build();
    }
}
