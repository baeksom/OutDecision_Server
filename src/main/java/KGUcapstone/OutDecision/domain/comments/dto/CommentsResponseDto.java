package KGUcapstone.OutDecision.domain.comments.dto;


import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import lombok.*;

import static KGUcapstone.OutDecision.global.util.DateTimeFormatUtil.formatCreatedAt2;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentsResponseDto {

    Long commentsId;
    String body;
    String nickname;
    String memberTitle;
    Long postId;
    String profileUrl;
    String createdAt;
    Boolean isOwn;;
}
