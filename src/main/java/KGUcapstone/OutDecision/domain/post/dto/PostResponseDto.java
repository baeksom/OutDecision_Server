package KGUcapstone.OutDecision.domain.post.dto;

import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import lombok.Getter;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter

public class PostResponseDto {

    private final Long id;
    private final String title;
    private final String content;
    private final Category category;
    private final Long member_id;
    private final int views;
    private final LocalDateTime createdAt, updatedAt;
    private final List<Comments> comments;
    private final Date deadline;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.views = post.getViews();
        this.member_id = post.getMember().getId();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.comments = post.getCommentsList();
        this.deadline = post.getDeadline();
    }




}
