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
                .fashionista(getTitleByCategory(memberId, Category.fashion))
                .foodie(getTitleByCategory(memberId, Category.food))
                .traveler(getTitleByCategory(memberId, Category.travel))
                .ceo(getTitleByCategory(memberId, Category.work))
                .romantist(getTitleByCategory(memberId, Category.love))
                .hobbyist(getTitleByCategory(memberId, Category.hobby))
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
            case fashion:
                missionCnt = missions.getFashionista_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "패셔니스타";
                break;
            case food:
                missionCnt = missions.getFoodie_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "미식가";
                break;
            case travel:
                missionCnt = missions.getTraveler_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "여행가";
                break;
            case work:
                missionCnt = missions.getCeo_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "일꾼";
                break;
            case love:
                missionCnt = missions.getRomantist_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "로맨티스트";
                break;
            case hobby:
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
