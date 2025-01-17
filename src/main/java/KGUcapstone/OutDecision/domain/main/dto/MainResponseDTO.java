package KGUcapstone.OutDecision.domain.main.dto;

import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.ranking.dto.RankingResponseDTO.RankingListDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MainResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostListDTO{
        List<PostDTO> hotPostList;
        List<PostDTO> latestPostList;
        List<PostDTO> closedPostList;
        List<PostDTO> imminentPostList;
        RankingListDTO rankingListDTO;
    }

}
