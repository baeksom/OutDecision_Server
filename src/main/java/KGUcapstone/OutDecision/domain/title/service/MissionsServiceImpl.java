package KGUcapstone.OutDecision.domain.title.service;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.title.domain.Missions;
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
    private final MissionsRepository missionsRepository;

    @Override
    public MemberMissionsDTO getMemberMissions(Long memberId) {
        return MemberMissionsDTO.builder()
                .memberId(memberId)
                .fashionista(getTitleByCategory(memberId, Category.FASHION))
                .foodie(getTitleByCategory(memberId, Category.FOOD))
                .traveler(getTitleByCategory(memberId, Category.TRAVEL))
                .ceo(getTitleByCategory(memberId, Category.WORK))
                .romantist(getTitleByCategory(memberId, Category.LOVE))
                .hobbyist(getTitleByCategory(memberId, Category.HOBBY))
                .greedy(getGreedyMission(memberId))
                .build();
    }

    @Override
    public TitleMissionsDTO getGreedyMission(Long memberId) {
        String title = "욕심쟁이";
        Missions missions = missionsRepository.findAllByMemberId(memberId);
        int greedyCnt = missions.getGreedy_cnt();

        return TitleMissionsDTO.builder()
                .title(title)
                .MissionCnt(greedyCnt)
                .build();
    }

    @Override
    public TitleMissionsDTO getTitleByCategory(Long memberId, Category category) {
        Missions missions = missionsRepository.findAllByMemberId(memberId);
        String title;
        int missionCnt = 0;
        int greedyCnt = missions.getGreedy_cnt();
        switch (category) {
            case FASHION:
                missionCnt = missions.getFashionista_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "패셔니스타";
                break;
            case FOOD:
                missionCnt = missions.getFoodie_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "미식가";
                break;
            case TRAVEL:
                missionCnt = missions.getTraveler_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "여행가";
                break;
            case WORK:
                missionCnt = missions.getCeo_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "일꾼";
                break;
            case LOVE:
                missionCnt = missions.getRomantist_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "로맨티스트";
                break;
            case HOBBY:
                missionCnt = missions.getHobbyist_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "취미가";
                break;
            default:
                title = "";
        }
        return TitleMissionsDTO.builder()
                .title(title)
                .MissionCnt(missionCnt)
                .build();
    }
}
