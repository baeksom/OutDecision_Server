package KGUcapstone.OutDecision.domain.post.controller;

import KGUcapstone.OutDecision.domain.post.dto.PostRequestDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.post.service.PostServiceImpl;

import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostApiController {

    private final PostServiceImpl postServiceImpl;

    /* 등록 */
    @PostMapping("")
    /*public ResponseEntity save(@RequestBody PostRequestDto dto) {
        //Member의 id 가져와서 Post의 member_id에 저장필요 => 어떤 사람이 했는지 알기위해??
        return ResponseEntity.ok(postService.save(dto));
    }*/
    public Long save(@RequestBody PostRequestDTO dto) {
        return postServiceImpl.save(dto);
    }

    /* 조회 */
    @GetMapping("/{postId}")
    @Operation(summary = "게시글 조회 API", description = "상세 게시글을 조회합니다.")
    public ApiResponse<PostDTO> viewPost(@PathVariable Long postId) {
        return ApiResponse.onSuccess(postServiceImpl.viewPost(postId));
    }

    /* 수정 */

    /*public ResponseEntity update(@PathVariable Long post_id, @RequestBody PostRequestDto dto) {
        postService.update(post_id, dto);
        return ResponseEntity.ok(post_id);
    }*/
    @PutMapping("/{postId}")
    public Long update(@PathVariable Long postId, @RequestBody PostRequestDTO dto) {
        return postServiceImpl.update(postId, dto);
    }

    /* 삭제 */
    @DeleteMapping("/{postId}")
    public ApiResponse<Object> deletePost(@PathVariable Long postId) {
        return ApiResponse.onSuccess(postServiceImpl.deletePost(postId));
    }

    /*public ResponseEntity delete(@PathVariable Long post_id) {
        postService.delete(post_id);
        return ResponseEntity.ok(post_id);
    }*/
    public Long delete(@PathVariable Long postId) {
        postServiceImpl.delete(postId);
        return postId;
    }
}
