package KGUcapstone.OutDecision.domain.user.controller;

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

    @DeleteMapping("/user/revoke")
    public ApiResponse<Object> revokeMemberController(@RequestHeader("Authorization") String accessToken) throws IOException {
        revokeService.deleteAccount(accessToken);
        return ApiResponse.onSuccess(null);
    }
}
