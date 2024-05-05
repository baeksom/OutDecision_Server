package KGUcapstone.OutDecision.domain.post.dto;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Gender;
import lombok.*;

import java.util.Date;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class PostRequestDTO {


    private String title;
    private String content;
    private Category category;
    private Date deadline;
    private Gender gender;

    /* Dto -> Entity */
    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .category(category)
                .deadline(deadline)
                .gender(gender)
                .build();

    }

}
