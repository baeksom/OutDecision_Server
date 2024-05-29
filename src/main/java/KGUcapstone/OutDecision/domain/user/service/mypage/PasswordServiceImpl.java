package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdatePasswordDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.global.error.exception.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
public class PasswordServiceImpl implements PasswordService{
    private final MemberRepository memberRepository;
    private final FindMemberService findMemberService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean updatePassword(UpdatePasswordDTO request) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Member member;
        // 로그인 체크
        if(memberOptional.isPresent()) member = memberOptional.get();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        // 현재 비밀번호와 불일치
        if(!passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) return false;
        
        // 현재 비밀번호와 새 비밀번호 일치
        if(request.getCurrentPassword().equals(request.getNewPassword())) return false;

        // 새 비밀번호와 새 비밀번호 불일치
        if(!request.getNewPassword().equals(request.getConfirmNewPassword())) return false;

        // 비밀번호 변경
        member.updatePassword(passwordEncoder.encode(request.getNewPassword()));

        memberRepository.save(member);

        return true;
    }

}
