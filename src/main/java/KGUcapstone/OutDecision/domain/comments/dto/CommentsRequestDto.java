package KGUcapstone.OutDecision.domain.comments.dto;


import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder

public class CommentsRequestDto {

    private Long id;
    private String body;
    private Member member;
    private Post post;

    public Comments toEntity(){

        return Comments.builder()
                .id(id)
                .body(body)
                .member(member)
                .post(post)
                .build();
    }
}
