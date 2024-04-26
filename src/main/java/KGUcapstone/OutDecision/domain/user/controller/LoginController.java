package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/loginSuccess")
    public ApiResponse<Object> loginSuccess(@CookieValue(name = "accessToken", required = true) String accessToken,
                                            HttpServletResponse response) {
        // 응답 헤더에 accessToken을 포함하여 클라이언트에게 반환
        response.setHeader("Authorization", accessToken);
        return ApiResponse.onSuccess(null);
    }
}
