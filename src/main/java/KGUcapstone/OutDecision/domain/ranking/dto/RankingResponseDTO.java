package KGUcapstone.OutDecision.domain.ranking.dto;

import lombok.*;

import java.util.List;

public class RankingResponseDTO {
    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RankingDTO{
        Integer rank;
        Long memberId;
        String userImg;
        String nickname;
        Integer point;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RankingListDTO{
        List<RankingDTO> RankingList;
        Integer listSize;
    }
}