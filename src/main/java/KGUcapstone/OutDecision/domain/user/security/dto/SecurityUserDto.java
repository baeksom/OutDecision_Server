package KGUcapstone.OutDecision.domain.user.security.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
@Builder
public class SecurityUserDto {
    private String email;
    private String nickname;
    private String role;
    private Long memberId;
}
