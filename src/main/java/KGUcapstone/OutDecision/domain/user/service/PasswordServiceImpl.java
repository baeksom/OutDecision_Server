package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdatePasswordDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateResponseDTO.UpdatePasswordResultDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.global.error.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.handler.PasswordHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class PasswordServiceImpl implements PasswordService{
    private final MemberRepository memberRepository;

    @Override
    public UpdatePasswordResultDTO updatePassword(Long memberId, UpdatePasswordDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 비밀번호 변경
        member.updatePassword(request.getNewPassword());

        memberRepository.save(member);

        return UpdatePasswordResultDTO.builder()
                .message("비밀번호 변경이 완료되었습니다.")
                .build();

    }

}
