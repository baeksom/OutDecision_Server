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

    @PostMapping("/{postId}/comments")
    @Operation(summary = "댓글 작성", description = "댓글을 작성하여 등록합니다.")
    public ResponseEntity<ApiResponse<Comments>> createComment(@PathVariable Long postId, @RequestBody CommentsRequestDto dto){
        commentsService.save(dto.getNickname(), postId, dto);
        return ResponseEntity.ok(ApiResponse.onSuccess((Comments) commentsService.findAll(postId)));
    }

    @GetMapping("/{postId}/comments")
    @Operation(summary = "댓글 조회", description = "게시글의 댓글들을 조회하여 보여줍니다.")
    public ResponseEntity<List<CommentsResponseDto>> getCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentsService.findAll(postId));
    }

    @DeleteMapping("/{postId}/comments/{commentsId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    public ResponseEntity<ApiResponse<Long>> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentsService.delete(postId, commentId);
        return ResponseEntity.ok(ApiResponse.onSuccess(commentId));
    }

}
