package KGUcapstone.OutDecision.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponseStatus {

    private Integer status;
    private String accessToken;

    public static TokenResponseStatus addStatus(Integer status, String accessToken) {
        return new TokenResponseStatus(status, accessToken);
    }
}