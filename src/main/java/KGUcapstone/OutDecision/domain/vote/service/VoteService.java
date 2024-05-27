package KGUcapstone.OutDecision.domain.vote.service;

import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO;
import KGUcapstone.OutDecision.domain.vote.dto.VoteResponseDto;

import java.util.List;

public interface VoteService {
    VoteResponseDto.VoteResultDTO addVote(List<Long> optionsIds);
}
