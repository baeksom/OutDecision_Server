package KGUcapstone.OutDecision.domain.user.security.dto;

import lombok.*;

@NoArgsConstructor
@Getter
@ToString
@AllArgsConstructor
@Builder
public class GeneratedToken {

    private String accessToken;
    private String refreshToken;
}