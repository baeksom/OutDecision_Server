package KGUcapstone.OutDecision.domain.user.dto;

import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MemberResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageDTO {
        Long memberId;
        String nickname;
        String userImg;
        Integer titleCnt;
        Integer point;
        List<PostDTO> postList;
    }

}
