package KGUcapstone.OutDecision.domain.vote.service;

import KGUcapstone.OutDecision.domain.options.domain.Options;
import KGUcapstone.OutDecision.domain.options.repository.OptionsRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO;
import KGUcapstone.OutDecision.domain.post.service.PostService;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.user.service.mypage.TitleService;
import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import KGUcapstone.OutDecision.domain.vote.dto.VoteResponseDto;
import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
import KGUcapstone.OutDecision.global.error.exception.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.exception.handler.OptionHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class VoteServiceImpl implements VoteService{

    private final VoteRepository voteRepository;
    private final FindMemberService findMemberService;
    private final OptionsRepository optionsRepository;
    private final TitleService titleService;
    private final PostService postService;

    @Override
    public VoteResponseDto.VoteResultDTO addVote(List<Long> optionsIds) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Member member;
        // 로그인 체크
        if(memberOptional.isPresent()) { member = memberOptional.get(); }
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        List<Options> options = new ArrayList<>();

        Post post = null;
        for(Long id : optionsIds) {
            Optional<Options> options1 = optionsRepository.findById(id);
            post = options1.get().getPost();
            break;
        }

        if (!voteRepository.findByMemberIdAndPostId(member.getId(), post.getId()).isEmpty()) {
            throw new OptionHandler(ErrorStatus.VOTE_FORBIDDEN);
        }

        for (Long id : optionsIds) {
            Optional<Options> option = optionsRepository.findById(id);

            if (option.isPresent()) {
                options.add(option.get());

                // 투표 저장
                Vote vote = Vote.builder()
                        .member(member)
                        .options(option.get())
                        .build();
                voteRepository.save(vote);

                System.out.println("-- 투표 완료 --");
            } else {
                System.out.println("옵션이 없음");
                throw new OptionHandler(ErrorStatus.OPTION_NOT_FOUND);
            }
        }

        post = options.get(0).getPost();

        // 총 투표 수 계산
        long totalVoteCnt = post.getOptionsList().stream()
                // 게시글에 대한 옵션에서 해당 옵션에 투표된 개수를 구함
                .mapToLong(option -> option.getVoteList().size())
                .sum();

        // DTO 만들기
        List<PostsResponseDTO.OptionsDTO> optionsDtoList = post.getOptionsList().stream()
                .map(option -> {
                    // 해당 option의 투표 수 계산
                    long optionVoteCnt = option.getVoteList().size();

                    // 투표 결과 퍼센트 계산 (소수점 없음)
                    int votePercentage = (int) Math.round((optionVoteCnt * 100.0) / totalVoteCnt);

                    return PostsResponseDTO.OptionsDTO.builder()
                            .optionId(option.getId())
                            .body(option.getBody())
                            .imgUrl(option.getPhotoUrl())
                            .votePercentage(votePercentage)
                            .build();
                })
                .toList();

        // 포인트 +10
        member.updatePoint(member.getPoint() + 10);

        // 핫 게시글 가능 여부 확인
        postService.turnsHot(post);

        // 칭호 획득 가능 여부 확인
        titleService.memberGetTitle(post, member);

        return VoteResponseDto.VoteResultDTO.builder()
                .selectedOptions(optionsIds)
                .optionsList(optionsDtoList)
                .build();
    }
}
