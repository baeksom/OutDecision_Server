package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdatePasswordDTO;

import static KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.*;


public interface PasswordService {

    boolean updateNewPassword(UpdateNewPasswordDTO request);
    boolean updatePassword(UpdatePasswordDTO request);
}
