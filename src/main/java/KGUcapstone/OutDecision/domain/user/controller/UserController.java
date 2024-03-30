package KGUcapstone.OutDecision.domain.user.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user/register/v1")
@RequiredArgsConstructor
public class UserController {

    @GetMapping
//    public String showRegisterForm(@RequestParam("email") String email,
//                                   @RequestParam("provider") String provider,
//                                   Model model) {
    public String showRegisterForm() {
        // 받아온 파라미터 사용하기
//        model.addAttribute("email", email);
//        model.addAttribute("provider", provider);
//
//        System.out.println("email = " + email);
//        System.out.println("provider = " + provider);

        // 회원가입 폼 페이지로 이동
        return "register";
    }

    private final UserService userService;

    @PostMapping
    public String registerUser(HttpServletRequest request,
                               @RequestParam("name") String name,
                               @RequestParam("phoneNumber") String phoneNumber
                               ) {
//        System.out.println("email = " + email);
//        System.out.println("provider = " + provider);
        String email=null;
        String provider=null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("email")) {
                    email = cookie.getValue();
                    break; // 원하는 쿠키를 찾았으므로 반복문 종료
                }
            }
        }
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("provider")) {
                    provider = cookie.getValue();
                    break; // 원하는 쿠키를 찾았으므로 반복문 종료
                }
            }
        }

        System.out.println("email = " + email);
        System.out.println("provider = " + provider);
        System.out.println("name = " + name);
        System.out.println("phoneNumber = " + phoneNumber);

        userService.registerUser(email, provider, name, phoneNumber);

        // 회원가입 성공 페이지로 이동
        return "register-success";
    }
}

