package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO;

public interface MyPageService {
    MemberResponseDTO.MyPageDTO getMyPage(Long memberId, String posts);
}
