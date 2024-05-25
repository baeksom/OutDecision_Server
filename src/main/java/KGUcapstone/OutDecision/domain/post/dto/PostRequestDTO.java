package KGUcapstone.OutDecision.domain.post.dto;

import KGUcapstone.OutDecision.domain.post.domain.enums.Gender;
import lombok.Getter;

public class PostRequestDTO {

    @Getter
    public static class UploadPostDTO{
        String title;
        String content;
        String category;
        Gender gender;
        boolean pluralVoting;
        String deadline;
    }

}
