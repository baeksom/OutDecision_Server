package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO;

import static KGUcapstone.OutDecision.domain.user.dto.UpdateResponseDTO.*;

public interface MemberService {
    boolean updateMemberInfo(UpdateRequestDTO.UpdateMemberDTO request);
    MemberInfoDTO getMemberById();
}
