package KGUcapstone.OutDecision.domain.user.security.dto;

import lombok.*;

@Getter
public class RegisterRequestDto {
    private String name;
    private String nickname;
    private String email;
    private String password;
    private String phone;
    private String userImg;
}
