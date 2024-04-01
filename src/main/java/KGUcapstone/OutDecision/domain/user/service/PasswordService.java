package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdatePasswordDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateResponseDTO.UpdatePasswordResultDTO;


public interface PasswordService {
    UpdatePasswordResultDTO updatePassword(Long memberId, UpdatePasswordDTO request);
}
