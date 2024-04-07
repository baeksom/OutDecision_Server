package KGUcapstone.OutDecision.domain.user.controller;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.MyPostListDTO;
import KGUcapstone.OutDecision.domain.user.service.MyPostService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MemberRestController {
    private final MyPostService myPostService;

    @GetMapping("/{memberId}/posting")
    @Operation(summary = "마이페이지 작성글 API", description = "사용자가 작성한 글을 게시판 보드로 조회하는 API이며, 페이징을 포함합니다.")
    public ApiResponse<MyPostListDTO> getMyPostList(
            @PathVariable(name = "memberId") Long memberId,
            @RequestParam(name = "status", required = false) Status status,
            @RequestParam(name = "page", defaultValue = "1") Integer page) {
        // 페이지 번호를 1부터 시작하도록 변환
        Integer adjustedPage = page - 1;
        Page<Post> myPostPage = myPostService.getMyPostListByStatus(memberId, status, adjustedPage);
        return ApiResponse.onSuccess(myPostService.myPostListDTO(myPostPage));
    }
}
