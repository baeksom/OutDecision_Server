package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO;

import java.util.List;

public interface TitleService {
    // 프로필 사진 변경
    boolean updateUserTitle(Long memberId, UpdateRequestDTO.UpdateTitleDTO request);

    List<String> myTitlesDTO(Long memberId);
}
