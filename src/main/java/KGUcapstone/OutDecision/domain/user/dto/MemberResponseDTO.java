package KGUcapstone.OutDecision.domain.user.dto;

import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MemberResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageDTO {
        Long memberId;
        String nickname;
        String userImg;
        String memberTitle;
        Integer titleCnt;
        Integer point;
        Integer bumps;
        List<PostDTO> postList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginSuccessMemberDTO {
        Long memberId;
        String nickname;
        String userImg;
        String memberTitle;
        Integer titleCnt;
        Integer point;
    }

}
