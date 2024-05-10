package KGUcapstone.OutDecision.domain.likes.controller;

import KGUcapstone.OutDecision.domain.likes.service.LikesService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/{arg0}/like")
    @Operation(summary = "좋아요 API", description = "게시글에 좋아요를 누릅니다.")
    public ApiResponse<?> addLike(@PathVariable Long arg0) {
        Long likesId = likesService.addLikes(arg0);
        return ApiResponse.onSuccess(likesId);
    }
}
