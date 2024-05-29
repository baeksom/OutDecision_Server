package KGUcapstone.OutDecision.domain.comments.controller;


import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.comments.dto.CommentsRequestDto;
import KGUcapstone.OutDecision.domain.comments.dto.CommentsResponseDto;
import KGUcapstone.OutDecision.domain.comments.service.CommentsService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import KGUcapstone.OutDecision.global.util.DateTimeFormatUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static KGUcapstone.OutDecision.global.util.DateTimeFormatUtil.formatCreatedAt2;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentsApiController {

    @Autowired
    private final CommentsService commentsService;

    @PostMapping("/{postId}/comments")
    @Operation(summary = "댓글 작성", description = "댓글을 작성하여 등록합니다.")
    public ApiResponse<CommentsResponseDto> createComment(@PathVariable Long postId, @RequestBody CommentsRequestDto dto) {
        CommentsResponseDto responseDto = commentsService.save(postId, dto);
        return ApiResponse.onSuccess(responseDto);
    }

    @DeleteMapping("/{postId}/comments/{commentsId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    public ApiResponse<Long> deleteComment(@PathVariable Long postId, @PathVariable Long commentsId) {
        commentsService.delete(postId, commentsId);
        return ApiResponse.onSuccess(commentsId);
    }
}
