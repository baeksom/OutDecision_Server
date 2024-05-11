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

    private String body;

    public Comments toEntity(Member member, Post post){

        return Comments.builder()
                .member(member)
                .body(body)
                .post(post)
                .build();
    }
}
