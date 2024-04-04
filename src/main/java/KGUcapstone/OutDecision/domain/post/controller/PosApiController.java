package KGUcapstone.OutDecision.domain.post.controller;

import KGUcapstone.OutDecision.domain.post.dto.PostRequestDto;
import KGUcapstone.OutDecision.domain.post.service.PostService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/posts")
@RequiredArgsConstructor
@RestController
public class PosApiController {
    private final PostService postService;

    /* 등록 */
    @PostMapping("")
    public ResponseEntity save(@RequestBody PostRequestDto dto) {
        //Member의 id 가져와서 Post의 member_id에 저장필요 => 어떤 사람이 했는지 알기위해??
        return ResponseEntity.ok(postService.save(dto));
    }

    /* 조회 */
    @GetMapping("/{post_id}")
    public ResponseEntity read(@PathVariable Long post_id) {
        return ResponseEntity.ok(postService.findById(post_id));
    }

    /* 수정 */
    @PutMapping("/{post_id}")
    public ResponseEntity update(@PathVariable Long post_id, @RequestBody PostRequestDto dto) {
        postService.update(post_id, dto);
        return ResponseEntity.ok(post_id);
    }

    /* 삭제 */
    @DeleteMapping("/{post_id}")
    public ResponseEntity delete(@PathVariable Long post_id) {
        postService.delete(post_id);
        return ResponseEntity.ok(post_id);
    }
}
