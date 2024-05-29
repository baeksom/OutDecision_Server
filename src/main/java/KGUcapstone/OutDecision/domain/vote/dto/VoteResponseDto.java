package KGUcapstone.OutDecision.domain.vote.dto;

import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class VoteResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoteResultDTO {
        List<Long> selectedOptions;
        List<PostsResponseDTO.OptionsDTO> optionsList;
    }
}
