package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateResponseDTO;

public interface UserImgService {

    UpdateResponseDTO.UpdateUserImgResultDTO updateUserImg(Long memberId, UpdateRequestDTO.UpdateUserImgDTO request);

    // 프로필 사진 삭제 -> 기본 이미지 변경
    UpdateResponseDTO.UpdateUserImgResultDTO deleteUserImg(Long memberId);
}
