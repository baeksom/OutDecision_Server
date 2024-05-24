package KGUcapstone.OutDecision.domain.comments.dto;


import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentsResponseDto {

    private Long id;
    private String body;
    private String nickname;
    private Long postId;
    private String profileUrl;

    public CommentsResponseDto(Comments comments){
        this.id = comments.getId();
        this.body = comments.getBody();
        this.nickname = comments.getMember().getNickname();
        this.postId = comments.getPost().getId();
        this.profileUrl = comments.getMember().getUserImg();
    }
}
