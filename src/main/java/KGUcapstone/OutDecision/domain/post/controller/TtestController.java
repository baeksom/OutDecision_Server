package KGUcapstone.OutDecision.domain.post.controller;

import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.post.service.PostService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TtestController {

    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping("/test/{arg0}")
    public ApiResponse<?> test(@PathVariable Long arg0) {
        postService.turnsHot(postRepository.findById(arg0).get());
        return ApiResponse.onSuccess(arg0);
    }
}
