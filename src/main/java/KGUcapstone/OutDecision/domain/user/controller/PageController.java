package KGUcapstone.OutDecision.domain.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class PageController {

    @GetMapping("/register/success")
    public String showSuccessRegister() {
        return "register-success";
    }

    @GetMapping("/register/v2")
    public String showNormalRegisterForm(){
        return "normal-register";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

//    @GetMapping("/register/v1")
//    public String showSocialRegisterForm(HttpServletRequest request,
//                                                      @CookieValue(name = "email", required = true) String email,
//                                                      @CookieValue(name = "provider", required = true) String provider) throws Exception {
//        System.out.println("controller joinSecret = " + joinSecret);
//
//        String join_token = request.getParameter("join_token");
//        System.out.println("controller receive join_token = " + join_token);
//        if(!AESUtil.decrypt(join_token, joinSecret).equals(email+provider)){
//            return "login";
//        }
//
//        return "social-register";
//    }
}
