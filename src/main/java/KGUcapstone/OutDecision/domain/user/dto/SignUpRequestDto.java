package KGUcapstone.OutDecision.domain.user.dto;

import lombok.*;

@Getter
public class SignUpRequestDto {
    private String name;
    private String nickname;
    private String email;
    private String password;
    private String phone;
    private String userImg;
}
