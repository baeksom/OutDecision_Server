package KGUcapstone.OutDecision.domain.post.dto;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PostsResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDto {
        Long id;
        String postTitle;
        Category category;
        Status stats;
        Long userId;
        String nickname;
        Integer likes;
        Integer views;
        Integer comments;
        LocalDateTime createAt;
    }

}
