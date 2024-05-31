package KGUcapstone.OutDecision.domain.title.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MissionsResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberMissionsDTO{
        Long memberId;
        TitleMissionsDTO fashionista;
        TitleMissionsDTO foodie;
        TitleMissionsDTO traveler;
        TitleMissionsDTO ceo;
        TitleMissionsDTO romantist;
        TitleMissionsDTO hobbyist;
        TitleMissionsDTO greedy;
        TitleRankDTO first;
        TitleRankDTO second;
        TitleRankDTO third;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TitleMissionsDTO{
        String title;
        Integer MissionCnt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TitleRankDTO{
        String title;
        boolean isOwn;
    }
}
