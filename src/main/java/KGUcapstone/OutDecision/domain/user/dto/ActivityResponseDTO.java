package KGUcapstone.OutDecision.domain.user.dto;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class ActivityResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPostDTO{
        Long postId;
        String title;
        Category category;
        Status status;
        LocalDateTime createdAt;
        Date deadline;
        List<String> optionsList;
        Integer commentsCnt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPostListDTO{
        List<MyPostDTO> myPostList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikedPostDTO{
        Long postId;
        String title;
        Category category;
        Status status;
        LocalDateTime createdAt;
        Date deadline;
        List<String> optionsList;
        Integer commentsCnt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LikedPostListDTO{
        List<LikedPostDTO> likedPostList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyCommentDTO{
        Long postId;
        String title;
        String body;
        Category category;
        Status status;
        LocalDateTime createdAt;
        Integer commentsCnt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyCommentListDTO{
        List<MyCommentDTO> myCommentList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }
}
