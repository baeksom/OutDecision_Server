package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.user.service.auth.RevokeService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RevokeController {

    private final RevokeService revokeService;
    private final FindMemberService findMemberService;

    @DeleteMapping("/user/revoke")
    public ApiResponse<Object> revokeMemberController() throws IOException {
        String accessToken = findMemberService.getTokenFromCookies();
        revokeService.deleteAccount(accessToken);
        return ApiResponse.onSuccess(null);
    }
}
