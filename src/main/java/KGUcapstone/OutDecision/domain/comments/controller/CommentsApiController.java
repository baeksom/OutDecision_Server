package KGUcapstone.OutDecision.domain.comments.controller;


import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.comments.dto.CommentsRequestDto;
import KGUcapstone.OutDecision.domain.comments.dto.CommentsResponseDto;
import KGUcapstone.OutDecision.domain.comments.service.CommentsService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class CommentsApiController {

    @Autowired
    private final CommentsService commentsService;

    @PostMapping("/{arg0}/comments")
    @Operation(summary = "댓글 작성", description = "댓글을 작성하여 등록합니다.")
    public ResponseEntity<ApiResponse<CommentsResponseDto>> createComment(@PathVariable Long arg0, @RequestBody CommentsRequestDto dto){
        Comments comments = commentsService.save(arg0, dto);
        CommentsResponseDto responseDto = new CommentsResponseDto(comments);
        return ResponseEntity.ok(ApiResponse.onSuccess(responseDto));
    }

    @DeleteMapping("/{arg0}/comments/{arg1}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    public ResponseEntity<ApiResponse<Long>> deleteComment(@PathVariable Long arg0, @PathVariable Long arg1) {
        commentsService.delete(arg0, arg1);
        return ResponseEntity.ok(ApiResponse.onSuccess(arg1));
    }

}
