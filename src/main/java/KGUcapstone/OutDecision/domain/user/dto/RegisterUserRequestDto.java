package KGUcapstone.OutDecision.domain.user.dto;

import lombok.Getter;

public class RegisterUserRequestDto {
    @Getter
    public static class JoinDTO{
        private String nickname;
        private String phone;
    }
}
