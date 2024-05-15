package KGUcapstone.OutDecision.domain.main.dto;

import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MainResponseReDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostListReDTO{
        List<PostDTO> recommendPostList;
    }

}

