package KGUcapstone.OutDecision.domain.post.dto;

import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Gender;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import lombok.Getter;


import java.time.LocalDateTime;
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
    private final Status status;
    private final Integer views;
    private final LocalDateTime bumpsTime;
    private final String nickname;
    private final boolean pluralVoting;
    private final Long user_id;


    public PostResponseDto(Post post) {

        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
        this.comments = post.getCommentsList();
        this.deadline = post.getDeadline();
        this.gender = post.getGender();
        this.status = post.getStatus();
        this.views = post.getViews();
        this.bumpsTime = post.getBumpsTime();
        this.nickname = post.getMember().getNickname();
        this.pluralVoting = post.getPluralVoting();
        this.user_id = post.getMember().getId();


    }




}
