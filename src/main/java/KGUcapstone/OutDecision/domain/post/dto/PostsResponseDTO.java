package KGUcapstone.OutDecision.domain.post.dto;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Gender;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class PostsResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostDTO {
        Long postId;
        String title;
        String content;
        Category category;
        Status status;
        Gender gender;
        Long userId;
        String nickname;
        Boolean pluralVoting;
        String createdAt;
        String bumpsTime;
        String deadline;
        Integer participationCnt;
        Integer likesCnt;
        Integer commentsCnt;
        Integer views;
        LoginMemberPostInfoDTO loginMemberPostInfoDTOList;
        List<OptionsDTO> optionsList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginMemberPostInfoDTO {
        Boolean isLiked;
        Boolean receiveAlert;
        List<Long> votedOptionIds;
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
