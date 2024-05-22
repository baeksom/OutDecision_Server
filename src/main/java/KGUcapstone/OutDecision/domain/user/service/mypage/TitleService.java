package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO;

import java.util.List;

public interface TitleService {
    // 프로필 사진 변경
    boolean updateUserTitle(UpdateRequestDTO.UpdateTitleDTO request);

    List<String> myTitlesDTO(Long memberId);

    void memberGetTitle(Post post, Member member);
}
