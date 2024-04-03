package KGUcapstone.OutDecision.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class PageController {

    @GetMapping("/registerSuccess")
    public String showSuccessRegister() {
        return "register-success";
    }
    @GetMapping("/user/register/v1")
    public String showSocialRegisterForm() {
        return "social-register";
    }

    @GetMapping("/user/register/v2")
    public String showNormalRegisterForm(){
        return "normal-register";
    }

    @GetMapping("/login-test")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String showLoginSuccess(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String accessToken = authorization.substring(7); // "Bearer " 다음의 엑세스 토큰 추출
            log.info("Authorization header: " + accessToken);
        } else {
            log.error("Authorization header not found");
        }

        return "login-success";
    }

}
