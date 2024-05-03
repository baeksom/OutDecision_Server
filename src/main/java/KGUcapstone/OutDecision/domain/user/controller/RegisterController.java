package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.RegisterRequestDto;
import KGUcapstone.OutDecision.domain.user.service.auth.CustomUserDetailsService;
import KGUcapstone.OutDecision.domain.user.service.auth.CustomOAuth2UserService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import KGUcapstone.OutDecision.global.common.util.AESUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    @Value("${JOIN_SECRET}")
    String joinSecret;

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/register/v1")
    public ApiResponse<Object> registerSocialUser(@CookieValue(name = "email", required = false) String email,
                                    @CookieValue(name = "provider", required = false) String provider,
                                    @RequestParam("nickname") String nickname,
                                    @RequestParam("userImg") String userImg
                               ) {
        customOAuth2UserService.registerSocialMember(email, provider, nickname, userImg);
        return ApiResponse.onSuccess(null);
    }

    @PostMapping("/register/v2")
    public ApiResponse<Object> registerNormalUser(@RequestBody @Valid RegisterRequestDto request) {
        customUserDetailsService.saveMember(request);
        return ApiResponse.onSuccess(null);
    }

    @GetMapping("/register/v1")
    public ApiResponse<Object> showSocialRegisterForm(HttpServletRequest request,
                                                      @CookieValue(name = "email") String email,
                                                      @CookieValue(name = "provider") String provider) throws Exception {
        System.out.println("controller joinSecret = " + joinSecret);

        String join_token = request.getParameter("join_token");
        System.out.println("controller receive join_token = " + join_token);
        if(!AESUtil.decrypt(join_token, joinSecret).equals(email+provider)){
            return ApiResponse.onFailure("400", "잘못된 접근입니다.", null);
        }

        return ApiResponse.onSuccess(join_token);
    }
}
