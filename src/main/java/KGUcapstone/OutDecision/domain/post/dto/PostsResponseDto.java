package KGUcapstone.OutDecision.domain.post.dto;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PostsResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDTO {
        Long postId;
        String title;
        String content;
        Category category;
        Status stats;
        Long userId;
        String nickname;
        boolean pluralVoting;
        String createdAt;
        String deadline;
        Integer participationCnt;
        Integer likesCnt;
        Integer commentsCnt;
        Integer views;
        List<OptionsDTO> optionsList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OptionsDTO{
        String body;
        String imgUrl;
        Integer votePercentage;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostListDTO{
        List<PostDTO> postList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }
}
