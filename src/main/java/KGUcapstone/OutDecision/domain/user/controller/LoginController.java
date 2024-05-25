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
    public ApiResponse<Object> loginSuccess() {
        return ApiResponse.onSuccess(null);
    }
}