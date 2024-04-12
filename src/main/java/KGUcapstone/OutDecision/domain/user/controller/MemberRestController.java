package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO.MyPageDTO;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdateTitleDTO;
import KGUcapstone.OutDecision.domain.user.service.MyPageService;
import KGUcapstone.OutDecision.domain.user.service.TitleService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MemberRestController {
    private final MyPageService myPageService;
    private final TitleService titleService;

    @GetMapping("/{memberId}")
    @Operation(summary = "마이페이지 홈 API", description = "마이페이지 홈을 조회하는 API입니다.")
    public ApiResponse<MyPageDTO> getMyPostList(
            @PathVariable(name = "memberId") Long memberId,
            @RequestParam(name = "posts", required = false) String posts) {
        return ApiResponse.onSuccess(myPageService.getMyPage(memberId, posts));
    }

    @GetMapping("/{memberId}/title")
    @Operation(summary = "마이페이지 칭호 조회 API", description = "마이페이지에서 보유 칭호를 조회하는 API입니다.")
    public ApiResponse<List<String>> updateTitle(@PathVariable("memberId") Long memberId) {
        List<String> myTitlesDTO = titleService.myTitlesDTO(memberId);
        return ApiResponse.onSuccess(myTitlesDTO);
    }

    @PutMapping("/{memberId}/title")
    @Operation(summary = "마이페이지 칭호 변경 API", description = "마이페이지 홈에서 칭호를 변경하는 API입니다.")
    public ApiResponse<Object> updateTitle(@PathVariable("memberId") Long memberId, @RequestBody @Valid UpdateTitleDTO request) {
        boolean success = titleService.updateUserTitle(memberId, request);
        if (success) return ApiResponse.onSuccess("칭호가 성공적으로 변경되었습니다.");
        else return ApiResponse.onFailure("400", "칭호 변경에 실패하였습니다.", null);
    }
}
