package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.title.repository.TitleRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.UpdateRequestDTO.UpdateTitleDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TitleServiceImpl implements TitleService{
    private final MemberRepository memberRepository;
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
    public List<String> myTitlesDTO(Long memberId) {
        // 문자열로 컬럼 이름 받아오기
        String findTrueColumByMemberId = titleRepository.findTrueColumByMemberId(memberId);
        // 첫번째 글자 ',' 제거
        String titleStr = findTrueColumByMemberId.toString().substring(1);
        // ','로 문자열 분류하여 리스트 만들어 반환
        return Arrays.asList(titleStr.split(","));
    }

}
