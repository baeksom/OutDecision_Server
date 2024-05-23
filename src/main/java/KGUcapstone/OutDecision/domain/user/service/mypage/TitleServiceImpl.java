package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.title.domain.Missions;
import KGUcapstone.OutDecision.domain.title.domain.Title;
import KGUcapstone.OutDecision.domain.title.repository.TitleRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdateTitleDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.global.error.exception.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TitleServiceImpl implements TitleService{
    private final MemberRepository memberRepository;
    private final FindMemberService findMemberService;
    private final TitleRepository titleRepository;

    // 칭호 변경
    @Override
    public boolean updateUserTitle(Long memberId, UpdateTitleDTO request) {
        Member member = memberRepository.findById(memberId).get();

        member.updateUserTitle(request.getTitle());
        memberRepository.save(member);

        return true;
    }

    // 보유 칭호 조회
    @Override
    public List<String> myTitlesDTO() {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Long memberId;
        // 로그인 체크
        if(memberOptional.isPresent()) memberId = memberOptional.get().getId();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        // 문자열로 컬럼 이름 받아오기
        String findTrueColumByMemberId = titleRepository.findTrueColumByMemberId(memberId);
        // 첫번째 글자 ',' 제거
        String titleStr = findTrueColumByMemberId.toString().substring(1);
        // ','로 문자열 분류하여 리스트 만들어 반환
        return Arrays.asList(titleStr.split(","));
    }

    // 칭호 획득 가능 여부 확인
    @Override
    public void memberGetTitle(Post post, Member member) {
        Category category = post.getCategory();
        Missions missions = member.getMissions();
        Title title = member.getTitle();

        if (post.getHot()) {
            // 게시글 Hot 여부 확인 O
            // Missions cnt+1
            if (category.equals(Category.fashion)) {
                missions.setFashionista_cnt(missions.getFashionista_cnt() + 1);
                if (missions.getFashionista_cnt() == 10) title.setFashionista(true);
            } else if (category.equals(Category.food)) {
                missions.setFoodie_cnt(missions.getFoodie_cnt() + 1);
                if (missions.getFoodie_cnt() == 10) title.setFoodie(true);
            } else if (category.equals(Category.love)) {
                missions.setRomantist_cnt(missions.getRomantist_cnt() + 1);
                if (missions.getRomantist_cnt() == 10) title.setRomantist(true);
            } else if (category.equals(Category.hobby)) {
                missions.setHobbyist_cnt(missions.getHobbyist_cnt() + 1);
                if (missions.getHobbyist_cnt() == 10) title.setHobbyist(true);
            } else if (category.equals(Category.travel)) {
                missions.setTraveler_cnt(missions.getTraveler_cnt() + 1);
                if (missions.getTraveler_cnt() == 10) title.setTraveler(true);
            } else if (category.equals(Category.work)) {
                missions.setCeo_cnt(missions.getCeo_cnt() + 1);
                if (missions.getCeo_cnt() == 10) title.setCeo(true);
            } else {
                // 기타
                missions.setHobbyist_cnt(missions.getHobbyist_cnt() + 1);
                if (missions.getHobbyist_cnt() == 10) title.setHobbyist(true);
            }
        }
    }
}
