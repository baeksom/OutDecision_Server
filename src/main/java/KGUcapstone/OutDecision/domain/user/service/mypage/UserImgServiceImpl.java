package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdateUserImgDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
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
    public boolean updateUserImg(Long memberId, UpdateUserImgDTO request) {
        Member member = memberRepository.findById(memberId).get();

        member.updateUserImg(request.getNewImg());
        memberRepository.save(member);

        return true;
    }

    // 프로필 사진 삭제 -> 기본 이미지 변경
    @Override
    public boolean deleteUserImg(Long memberId) {
        Member member = memberRepository.findById(memberId).get();

        member.updateUserImg("기본 이미지 URL");
        memberRepository.save(member);

        return true;
    }
}
