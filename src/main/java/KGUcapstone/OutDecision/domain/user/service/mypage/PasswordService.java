package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdatePasswordDTO;


public interface PasswordService {
    boolean updatePassword(UpdatePasswordDTO request);
}
