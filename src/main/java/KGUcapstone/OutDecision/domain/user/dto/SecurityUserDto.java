package KGUcapstone.OutDecision.domain.user.dto;

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
