package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.likes.repository.LikesRepository;
import KGUcapstone.OutDecision.domain.post.converter.PostConverter;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.title.repository.TitleRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO.MyPageDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
import KGUcapstone.OutDecision.global.error.exception.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageServiceImpl implements MyPageService{
    private final PostRepository postRepository;
    private final FindMemberService findMemberService;
    private final LikesRepository likesRepository;
    private final VoteRepository voteRepository;
    private final TitleRepository titleRepository;
    private final PostConverter  postConverter;

    @Override
    public MyPageDTO getMyPage(String posts) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Member member;
        Long memberId;
        // 로그인 체크
        if(memberOptional.isPresent())
        {
            member = memberOptional.get();
            memberId = member.getId();
        }
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        int titleCnt = titleRepository.countTrueColumnsForMember(memberId);

        List<Post> latestPostList = postList(memberId, posts);
        List<PostDTO> latestPostDTOList = latestPostList.stream()
                .map(postConverter::toPostDTO)
                .collect(Collectors.toList());

        return MyPageDTO.builder()
                .memberId(memberId)
                .nickname(member.getNickname())
                .userImg(member.getUserImg())
                .memberTitle(member.getUserTitle())
                .titleCnt(titleCnt)
                .point(member.getPoint())
                .bumps(member.getBumps())
                .postList(latestPostDTOList)
                .build();
    }

    public List<Post> postList(Long memberId, String posts) {
        if (posts == null || posts.equals("written")) {
            // 작성한 최신 게시글 2개 조회
            return postRepository.findAllByMemberId(memberId, Sort.by(Sort.Direction.DESC, "bumpsTime")).stream()
                    .limit(2)
                    .collect(Collectors.toList());
        } else if (posts.equals("liked")) {
            // 좋아요한 최신 게시글 2개 조회
            List<Long> likedPostIds = likesRepository.findPostIdsByMemberIdOrderByCreatedAtDesc(memberId);
            return likedPostIds.stream()
                    .map(postRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .limit(2)
                    .collect(Collectors.toList());
        } else if (posts.equals("voted")) {
            // 투표한 최신 게시글 2개 조회
            List<Object[]> votedPostData = voteRepository.findDistinctPostIdsByMemberIdOrderByCreatedAtDesc(memberId);
            List<Long> votedPostIds = votedPostData.stream()
                    .map(data -> (Long) data[0])
                    .toList();

            return votedPostIds.stream()
                    .map(postRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .limit(2)
                    .collect(Collectors.toList());
        } else return null;
    }

}
