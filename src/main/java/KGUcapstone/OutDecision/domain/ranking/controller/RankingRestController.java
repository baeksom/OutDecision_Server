package KGUcapstone.OutDecision.domain.ranking.controller;

import KGUcapstone.OutDecision.domain.ranking.dto.RankingResponseDTO.RankingDTO;
import KGUcapstone.OutDecision.domain.ranking.dto.RankingResponseDTO.RankingListDTO;
import KGUcapstone.OutDecision.domain.ranking.service.RankingService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RankingRestController {
    private final RankingService rankingService;

    @GetMapping("")
    @Operation(summary = "포인트랭킹 조회 API", description = "포인트랭킹 100위까지 조회합니다.")
    public ApiResponse<RankingListDTO> getAllRanking() {
        RankingListDTO rankingListDTO = rankingService.getTop100Rankings();
        return ApiResponse.onSuccess(rankingListDTO);
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "포인트랭킹 조회(개인) API", description = "사용자의 포인트랭킹 순위를 조회합니다.")
    public ApiResponse<RankingDTO> getMemberRanking(@PathVariable("memberId") Long memberId) {
        RankingDTO memberRankingDTO = rankingService.memberRankingDTO(memberId);
        return ApiResponse.onSuccess(memberRankingDTO);
    }
}
