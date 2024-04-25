package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdatePasswordDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional
public class PasswordServiceImpl implements PasswordService{
    private final MemberRepository memberRepository;

    @Override
    public boolean updatePassword(Long memberId, UpdatePasswordDTO request) {
        Member member = memberRepository.findById(memberId).get();

        // 현재 비밀번호와 불일치
        
        // 현재 비밀번호와 새 비밀번호 일치
        if(request.getCurrentPassword().equals(request.getNewPassword())) return false;

        // 새 비밀번호와 새 비밀번호 불일치
        if(!request.getNewPassword().equals(request.getConfirmNewPassword())) return false;

        // 비밀번호 변경
        member.updatePassword(request.getNewPassword());

        memberRepository.save(member);

        return true;
    }

}
