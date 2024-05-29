package KGUcapstone.OutDecision.domain.comments.converter;

import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.comments.dto.CommentsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static KGUcapstone.OutDecision.global.util.DateTimeFormatUtil.formatCreatedAt2;

@Component
@RequiredArgsConstructor
public class CommentsConverter {

    public CommentsResponseDto toCommentsResponseDto(Comments comments){

        return CommentsResponseDto.builder()
                .commentsId(comments.getId())
                .body(comments.getBody())
                .nickname(comments.getMember().getNickname())
                .postId(comments.getPost().getId())
                .profileUrl(comments.getMember().getUserImg())
                .createdAt(formatCreatedAt2(comments.getCreatedAt()))
                .isOwn(true)
                .build();
    }

}
