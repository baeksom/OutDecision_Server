package KGUcapstone.OutDecision.domain.user.dto;

import lombok.*;

@Getter
public class RegisterRequestDto {
    private String name;
    private String nickname;
    private String email;
    private String password;
    private String userImg;
}
