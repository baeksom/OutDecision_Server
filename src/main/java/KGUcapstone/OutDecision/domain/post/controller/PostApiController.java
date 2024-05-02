package KGUcapstone.OutDecision.domain.post.controller;

import KGUcapstone.OutDecision.domain.post.dto.PostRequestDto;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDto;
import KGUcapstone.OutDecision.domain.post.service.PostService;

import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RequiredArgsConstructor
@RestController
public class PostApiController {
    @Autowired
    private final PostService postService;


    @PostMapping("")
    @Operation(summary = "게시글 등록", description = "게시글을 작성하고 등록합니다.")
    public ApiResponse<Object> savePost(@RequestBody PostRequestDto request, @CookieValue(name = "accessToken", required = true) String accessToken, HttpServletResponse response) {
        boolean success = postService.save(request, accessToken);
        if(success) return ApiResponse.onSuccess("등록");
        else return ApiResponse.onFailure("400", "게시글 등록에 실패하였습니다.", null);

    }


    @GetMapping("/{post_id}")
    @Operation(summary = "게시글 조회", description = "선택한 관심있는 게시글을 볼 수 있습니다.")
    public ApiResponse<PostResponseDto> getPost(@PathVariable Long post_id) {
        PostResponseDto postDTO = postService.get(post_id);
        return ApiResponse.onSuccess(postDTO);
    }


    @PutMapping("/{post_id}")
    @Operation(summary = "게시글 수정")
    public ApiResponse<Object> updatePost(@PathVariable Long post_id, @RequestBody PostRequestDto request) {
        boolean success = postService.update(post_id, request);
        if(success) return ApiResponse.onSuccess("게시글이 수정되었습니다.");
        else return ApiResponse.onFailure("400", "게시글 수정에 실패하였습니다.", null);
    }


    @DeleteMapping("/{post_id}")
    @Operation(summary = "게시글 삭제")
    public ApiResponse<Object> deletePost(@PathVariable Long post_id) {
        boolean deleted = postService.delete(post_id);
        if (deleted) return ApiResponse.onSuccess("게시글이 성공적으로 삭제되었습니다");
        else return ApiResponse.onFailure("400","게시글 삭제에 실패하였습니다.",null);
    }


}
