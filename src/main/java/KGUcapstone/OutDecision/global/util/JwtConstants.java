package KGUcapstone.OutDecision.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtConstants {
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_TYPE = "BEARER ";

    public static final String ACCESS = "AccessToken";
    public static final String REFRESH = "RefreshToken";
}