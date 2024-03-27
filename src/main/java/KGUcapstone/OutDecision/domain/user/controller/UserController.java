package KGUcapstone.OutDecision.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/user/register/v1")
    public String showRegisterForm(@RequestParam("email") String email,
                                   @RequestParam("provider") String provider,
                                   Model model) {
        // 받아온 파라미터 사용하기
        model.addAttribute("email", email);
        model.addAttribute("provider", provider);

        // 회원가입 폼 페이지로 이동
        return "register";
    }

    @PostMapping("/user/register/submit")
    public String registerUser(@RequestParam("email") String email,
                               @RequestParam("provider") String provider,
                               @RequestParam("nickname") String nickname,
                               @RequestParam("phoneNumber") String phoneNumber,
                               Model model) {
        // 받아온 파라미터 사용하기
        model.addAttribute("email", email);
        model.addAttribute("provider", provider);
        model.addAttribute("nickname", nickname);
        model.addAttribute("phoneNumber", phoneNumber);

        System.out.println("email = " + email);
        System.out.println("provider = " + provider);
        System.out.println("nickname = " + nickname);
        System.out.println("phoneNumber = " + phoneNumber);

        // 회원가입 성공 페이지로 이동
        return "register-success";
    }
}

