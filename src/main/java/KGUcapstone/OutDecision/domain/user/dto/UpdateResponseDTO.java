package KGUcapstone.OutDecision.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UpdateResponseDTO {

    // 마이페이지 개인정보수정 조회에서 사용되는 개인정보 DTO
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberInfoDTO{
        Long memberId;
        String name;
        String email;
        String nickname;
        String socialType;
        String userImg;
    }
}
