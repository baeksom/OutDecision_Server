package KGUcapstone.OutDecision.domain.user.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieUtil {

    public void addCookie(HttpServletResponse response, String name, String value, int maxAge, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .path(path)
                .httpOnly(true)
                .secure(true)
                .maxAge(maxAge)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }
}
