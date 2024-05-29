package KGUcapstone.OutDecision.domain.vote.controller;

import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO;
import KGUcapstone.OutDecision.domain.vote.dto.VoteResponseDto;
import KGUcapstone.OutDecision.domain.vote.service.VoteService;
import KGUcapstone.OutDecision.global.error.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping("/vote")
    @Operation(summary = "투표 API", description = "특정 옵션을 투표합니다.")
    public ApiResponse<VoteResponseDto.VoteResultDTO> vote(@RequestBody List<Long> optionIds) {
        return ApiResponse.onSuccess(voteService.addVote(optionIds));
    }
}
