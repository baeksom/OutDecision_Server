package KGUcapstone.OutDecision.domain.ranking.service;

import KGUcapstone.OutDecision.domain.ranking.dto.RankingResponseDTO;

public interface RankingService {
    RankingResponseDTO.RankingListDTO getTop100Rankings();
    RankingResponseDTO.RankingDTO memberRankingDTO(Long memberId);
    RankingResponseDTO.RankingListDTO getTop10Rankings();
}
