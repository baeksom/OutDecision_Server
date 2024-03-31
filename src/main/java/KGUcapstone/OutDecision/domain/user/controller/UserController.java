package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/user/register/v1")
    public String showRegisterForm() {
        return "register";
    }

    private final MemberService memberService;

    @PostMapping("/user/register/v1")
    public String registerUser(@CookieValue(name = "email", required = false) String email,
                               @CookieValue(name = "provider", required = false) String provider,
                               @RequestParam("name") String name,
                               @RequestParam("phoneNumber") String phoneNumber
                               ) {

        System.out.println("email = " + email);
        System.out.println("provider = " + provider);
        System.out.println("name = " + name);
        System.out.println("phoneNumber = " + phoneNumber);

        memberService.registerMember(email, provider, name, phoneNumber);

        // 회원가입 성공 페이지로 이동
        return "register-success";
    }

    @GetMapping("/loginSuccess")
    public String showSuccessLogin() {
        return "register-success";
    }

}

