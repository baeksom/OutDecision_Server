package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdatePasswordDTO;


public interface PasswordService {
    boolean updatePassword(Long memberId, UpdatePasswordDTO request);
}
