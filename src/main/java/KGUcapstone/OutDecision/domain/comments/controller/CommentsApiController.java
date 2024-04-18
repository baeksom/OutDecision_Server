package KGUcapstone.OutDecision.domain.comments.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class CommentsApiController {

    /* 등록 */
    @PostMapping("/posts/{post_id}/comments")
}
