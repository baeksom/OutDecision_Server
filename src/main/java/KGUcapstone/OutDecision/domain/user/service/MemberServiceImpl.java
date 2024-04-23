package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateResponseDTO.MemberInfoDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;

    @Override
    public MemberInfoDTO getMemberById(Long memberId) {
        Member member = memberRepository.findById(memberId).get();

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
