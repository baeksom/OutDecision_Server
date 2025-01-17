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
        TitleMissionsDTO first;
        TitleMissionsDTO second;
        TitleMissionsDTO third;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TitleMissionsDTO{
        String title;
        Integer missionCnt;
    }

}
