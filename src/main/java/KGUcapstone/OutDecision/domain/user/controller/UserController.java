package KGUcapstone.OutDecision.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/register/v1")
    public String showRegisterForm(@RequestParam("email") String email,
                                   @RequestParam("provider") String provider,
                                   Model model) {
        // 받아온 파라미터 사용하기
        model.addAttribute("email", email);
        model.addAttribute("provider", provider);

        System.out.println("email = " + email);
        System.out.println("provider = " + provider);

        // 회원가입 폼 페이지로 이동
        return "register";
    }

    private final UserService userService;

    @PostMapping("/register/v1")
    public String registerUser(@RequestParam("email") String email,
                               @RequestParam("provider") String provider,
                               @RequestParam("name") String name,
                               @RequestParam("phoneNumber") String phoneNumber,
                               Model model) {
        System.out.println("email = " + email);
        System.out.println("provider = " + provider);
        System.out.println("name = " + name);
        System.out.println("phoneNumber = " + phoneNumber);

        userService.registerUser(email, provider, name, phoneNumber);

        // 회원가입 성공 페이지로 이동
        return "register-success";
    }
}

