package KGUcapstone.OutDecision.domain.post.dto;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Gender;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;



@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data

public class PostRequestDto {


    private String title;
    private String content;
    private Category category;
    private Date deadline;
    private Gender gender;
    private Status status;
    private Integer views;
    private Member member;
    private boolean pluralVoting;
    private LocalDateTime bumpsTime;

    /* Dto -> Entity */
    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .category(category)
                .deadline(deadline)
                .gender(gender)
                .status(status)
                .views(views)
                .bumpsTime(bumpsTime)
                .member(member)
                .build();


    }

}
