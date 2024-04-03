package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.SignUpRequestDto;
import KGUcapstone.OutDecision.domain.user.service.MemberService;
import KGUcapstone.OutDecision.domain.user.service.UserService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final MemberService memberService;
    private final UserService userService;

    @PostMapping("/user/register/v1")
    public ApiResponse<Object> registerSocialUser(@CookieValue(name = "email", required = false) String email,
                                    @CookieValue(name = "provider", required = false) String provider,
                                    @RequestParam("nickname") String nickname,
                                    @RequestParam("userImg") String userImg
                               ) {
        memberService.registerMember(email, provider, nickname, userImg);
        return ApiResponse.onSuccess(null);
    }

    @PostMapping("/user/register/v2")
    public ApiResponse<Object> registerNormalUser(@RequestBody @Valid SignUpRequestDto request) {
        userService.saveMember(request);
        return ApiResponse.onSuccess(null);
    }
}

