package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.MemberRequestDTO;
import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.global.error.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    public MemberResponseDTO.MemberDTO getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberResponseDTO.MemberDTO.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .build();

    }

    @Override
    public MemberResponseDTO.MemberDTO updateMemberInfo(Long memberId, MemberRequestDTO.UpdateMemberDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        member.updateMember(request.getName(), request.getNickname(), request.getPhone());

        memberRepository.save(member);

        return MemberResponseDTO.MemberDTO.builder()
                .memberId(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .phone(member.getPhone())
                .build();
    }
}
