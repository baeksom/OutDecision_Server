package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO.MyPageDTO;
import KGUcapstone.OutDecision.domain.user.service.MyPageService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MemberRestController {
    private final MyPageService myPageService;

    @GetMapping("/{memberId}")
    @Operation(summary = "마이페이지 홈 API", description = "마이페이지 홈을 조회하는 API입니다.")
    public ApiResponse<MyPageDTO> getMyPostList(
            @PathVariable(name = "memberId") Long memberId,
            @RequestParam(name = "posts", required = false) String posts) {
        return ApiResponse.onSuccess(myPageService.getMyPage(memberId, posts));
    }
}
