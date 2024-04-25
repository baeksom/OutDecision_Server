package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO;

public interface UserImgService {

    boolean updateUserImg(Long memberId, UpdateRequestDTO.UpdateUserImgDTO request);

    // 프로필 사진 삭제 -> 기본 이미지 변경
    boolean deleteUserImg(Long memberId);
}
