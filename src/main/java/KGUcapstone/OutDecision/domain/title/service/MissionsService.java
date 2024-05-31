package KGUcapstone.OutDecision.domain.title.service;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.title.dto.MissionsResponseDTO.MemberMissionsDTO;
import KGUcapstone.OutDecision.domain.title.dto.MissionsResponseDTO.TitleMissionsDTO;

public interface MissionsService {

    MemberMissionsDTO getMemberMissions();
    TitleMissionsDTO getGreedyMission(Long membeId);
    TitleMissionsDTO getTitleByCategory(Long memberId, Category category);
    TitleMissionsDTO getTitleByRank(Long memberId, String rank);
}
