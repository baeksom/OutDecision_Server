package KGUcapstone.OutDecision.domain.vote.controller;

import KGUcapstone.OutDecision.domain.vote.service.VoteService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/vote/{optionId}")
    @Operation(summary = "투표 API", description = "특정 옵션을 투표합니다.")
    public ApiResponse<?> vote(@PathVariable Long optionId) {
        if (voteService.addVote(optionId)) {
            return ApiResponse.onSuccess(null);
        }
        return ApiResponse.onFailure("400", "투표 실패하였습니다.", null);
    }

}
