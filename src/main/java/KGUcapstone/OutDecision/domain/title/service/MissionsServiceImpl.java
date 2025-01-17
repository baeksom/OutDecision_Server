package KGUcapstone.OutDecision.domain.title.service;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.title.domain.Missions;
import KGUcapstone.OutDecision.domain.title.dto.MissionsResponseDTO.MemberMissionsDTO;
import KGUcapstone.OutDecision.domain.title.dto.MissionsResponseDTO.TitleMissionsDTO;
import KGUcapstone.OutDecision.domain.title.repository.MissionsRepository;
import KGUcapstone.OutDecision.domain.title.repository.TitleRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.global.error.exception.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionsServiceImpl implements MissionsService{
    private final MissionsRepository missionsRepository;
    private final TitleRepository titleRepository;
    private final FindMemberService findMemberService;

    @Override
    public MemberMissionsDTO getMemberMissions() {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Long memberId;
        // 로그인 체크
        if(memberOptional.isPresent()) memberId = memberOptional.get().getId();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        // 욕심쟁이 초기화 설정
        Missions missions = missionsRepository.findAllByMemberId(memberId);
        missions.setGreedy_cnt(0);

        return MemberMissionsDTO.builder()
                .memberId(memberId)
                .fashionista(getTitleByCategory(memberId, Category.fashion))
                .foodie(getTitleByCategory(memberId, Category.food))
                .traveler(getTitleByCategory(memberId, Category.travel))
                .ceo(getTitleByCategory(memberId, Category.work))
                .romantist(getTitleByCategory(memberId, Category.love))
                .hobbyist(getTitleByCategory(memberId, Category.hobby))
                .greedy(getGreedyMission(memberId))
                .first(getTitleByRank(memberId, "first"))
                .second(getTitleByRank(memberId, "second"))
                .third(getTitleByRank(memberId, "third"))
                .build();
    }

    @Override
    public TitleMissionsDTO getGreedyMission(Long memberId) {
        String title = "욕심쟁이";
        Missions missions = missionsRepository.findAllByMemberId(memberId);
        int greedyCnt = missions.getGreedy_cnt();

        return TitleMissionsDTO.builder()
                .title(title)
                .missionCnt(greedyCnt)
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
                title = "트래블러";
                break;
            case work:
                missionCnt = missions.getCeo_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "사장";
                break;
            case love:
                missionCnt = missions.getRomantist_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "로맨티스트";
                break;
            case hobby:
                missionCnt = missions.getHobbyist_cnt();
                if (missionCnt >= 10) missions.setGreedy_cnt(greedyCnt+ 1);
                title = "취미부자";
                break;
            default:
                title = "";
        }
        return TitleMissionsDTO.builder()
                .title(title)
                .missionCnt(missionCnt)
                .build();
    }

    @Override
    public TitleMissionsDTO getTitleByRank(Long memberId, String rank) {
        String title = "";
        int missionCnt = 0;
        if (rank.equals("first") && titleRepository.findByMemberId(memberId).getFirst()) {
            title = "1위";
            missionCnt = 1;
        } else if (rank.equals("second") && titleRepository.findByMemberId(memberId).getSecond()) {
            title = "2위";
            missionCnt = 1;
        } else if (rank.equals("third") && titleRepository.findByMemberId(memberId).getThird()) {
            title = "3위";
            missionCnt = 1;
        }
        return TitleMissionsDTO.builder()
                .title(title)
                .missionCnt(missionCnt)
                .build();
    }
}
