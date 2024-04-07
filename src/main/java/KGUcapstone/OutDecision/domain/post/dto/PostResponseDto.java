package KGUcapstone.OutDecision.domain.post.dto;

import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Gender;
import lombok.Getter;


import java.util.Date;
import java.util.List;

@Getter

public class PostResponseDto {

    private final String title;
    private final String content;
    private final Category category;
    private final List<Comments> comments;
    private final Date deadline;
    private final Gender gender;

    public PostResponseDto(Post post) {

        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.comments = post.getCommentsList();
        this.deadline = post.getDeadline();
        this.gender = post.getGender();
    }




}
