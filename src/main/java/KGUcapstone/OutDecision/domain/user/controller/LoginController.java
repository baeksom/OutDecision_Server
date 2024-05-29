package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final FindMemberService findMemberService;

    @GetMapping("/loginSuccess")
    public ApiResponse<MemberResponseDTO.LoginSuccessMemberDTO> loginSuccess() {
        return ApiResponse.onSuccess(findMemberService.getLoginSuccessMember());
    }
}