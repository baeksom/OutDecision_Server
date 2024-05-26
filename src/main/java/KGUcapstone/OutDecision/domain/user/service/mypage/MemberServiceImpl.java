package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateResponseDTO.MemberInfoDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.global.error.exception.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;
    private final FindMemberService findMemberService;

    @Override
    public MemberInfoDTO getMemberById() {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Member member;
        // 로그인 체크
        if(memberOptional.isPresent()) member = memberOptional.get();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        return MemberInfoDTO.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .socialType(member.getSocialType())
                .userImg(member.getUserImg())
                .build();

    }

    @Override
    public boolean updateMemberInfo(Long memberId, UpdateRequestDTO.UpdateMemberDTO request) {
        Member member = memberRepository.findById(memberId).get();

        member.updateMember(request.getName(), request.getNickname());

        memberRepository.save(member);

        return true;
    }
}
