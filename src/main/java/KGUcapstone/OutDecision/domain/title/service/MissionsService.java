package KGUcapstone.OutDecision.domain.title.service;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.title.dto.MissionsResponseDTO.MemberMissionsDTO;
import KGUcapstone.OutDecision.domain.title.dto.MissionsResponseDTO.TitleMissionsDTO;

public interface MissionsService {

    MemberMissionsDTO getMemberMissions(Long memberId);

    TitleMissionsDTO getGreedyMission(Long memberId);

    TitleMissionsDTO getTitleByCategory(Long memberId, Category category);
}