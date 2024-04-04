package KGUcapstone.OutDecision.domain.post.dto;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PostRequestDto {

    private Long id;
    private String title;
    private String content;
    private Category category;
    private Integer views;
    private LocalDateTime createdAt, modifiedDate;
    private Member member;
    private Date deadline;

    /* Dto -> Entity */
    public Post toEntity() {
        return Post.builder()
                .id(id)
                .title(title)
                .content(content)
                .category(category)
                .views(views)
                .member(member)
                .deadline(deadline)
                .build();

    }

}
