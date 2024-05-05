package KGUcapstone.OutDecision.domain.post.dto;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Gender;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.OptionsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter

public class PostResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDTO {
        String title;
        String content;
        Category category;
        Status status;
        Gender gender;
        Long userId;
        String nickname;
        boolean pluralVoting;
        String createdAt;
        String deadline;
        Integer participationCnt;
        Integer likesCnt;
        Integer views;
        List<OptionsDTO> optionsList;
        CommentsListDTO commentsList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentsDTO {
        Long memberId;
        String nickname;
        String body;
        String createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentsListDTO {
        List<CommentsDTO> commentsDTOList;
        Integer listSize;
    }

}
