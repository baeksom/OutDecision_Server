package KGUcapstone.OutDecision.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberDTO{
        Long memberId;
        String name;
        String email;
        String nickname;
        String phone;
    }
}
