package KGUcapstone.OutDecision.domain.title.service;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.title.dto.MissionsResponseDTO.MemberMissionsDTO;
import KGUcapstone.OutDecision.domain.title.dto.MissionsResponseDTO.TitleMissionsDTO;
import KGUcapstone.OutDecision.domain.title.repository.MissionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionsServiceImpl implements MissionsService{
    private final PostRepository postRepository;
    private final MissionsRepository missionsRepository;

    @Override
    public MemberMissionsDTO getMemberMissions(Long memberId) {
        return MemberMissionsDTO.builder()
                .memberId(memberId)
                .build();
    }

    @Override
    public TitleMissionsDTO getTitleMission(Long memberId, Category category) {
        String title ="";
        int missionCnt = postRepository.getCntHotByMemberIdAndCategory(memberId, category);
        switch (category) {
            case FASHION:
                title = "패셔니스타";
                break;
            case FOOD:
                title = "미식사";
                break;
            case TRAVEL:
                title = "트래블러";
                break;
            case WORK:
                title = "사장";
                break;
            case LOVE:
                title = "로맨티스트";
                break;
        }
        return TitleMissionsDTO.builder()
                .title(title)
                .MissionCnt(missionCnt)
                .build();

    }

    @Override
    public TitleMissionsDTO getGreedyMission(Long memberId) {
        String title = "욕심쟁이";
        int fashionista_cnt = Math.min(postRepository.getCntHotByMemberIdAndCategory(memberId, Category.FASHION), 10);
        int foodie_cnt = Math.min(postRepository.getCntHotByMemberIdAndCategory(memberId, Category.FOOD), 10);
        int traveler_cnt = Math.min(postRepository.getCntHotByMemberIdAndCategory(memberId, Category.TRAVEL), 10);
        int ceo_cnt = Math.min(postRepository.getCntHotByMemberIdAndCategory(memberId, Category.WORK), 10);
        int romantist_cnt = Math.min(postRepository.getCntHotByMemberIdAndCategory(memberId, Category.LOVE), 10);
        int sum = 0;
        if (fashionista_cnt == 10) sum++;
        if (foodie_cnt == 10) sum++;
        if (traveler_cnt == 10) sum++;
        if (ceo_cnt == 10) sum++;
        if (romantist_cnt == 10) sum++;


    }
}
