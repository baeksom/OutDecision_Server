package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdateUserImgDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateResponseDTO.UpdateUserImgResultDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.global.error.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserImgServiceImpl implements UserImgService{
    private final MemberRepository memberRepository;

    // 프로필 사진 변경
    @Override
    public UpdateUserImgResultDTO updateUserImg(Long memberId, UpdateUserImgDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        member.updateUserImg(request.getNewImg());
        memberRepository.save(member);

        return UpdateUserImgResultDTO.builder()
                .newImg(request.getNewImg())
                .build();
    }

    // 프로필 사진 삭제 -> 기본 이미지 변경
    @Override
    public UpdateUserImgResultDTO deleteUserImg(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        member.updateUserImg("기본 이미지 URL");
        memberRepository.save(member);

        return UpdateUserImgResultDTO.builder()
                .newImg("기본 이미지 URL")
                .build();
    }
}
