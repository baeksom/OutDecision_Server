package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.likes.repository.LikesRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.title.repository.TitleRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.OptionsDTO;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.user.dto.MemberResponseDTO.MyPageDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageServiceImpl implements MyPageService{
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final VoteRepository voteRepository;
    private final TitleRepository titleRepository;

    @Override
    public MyPageDTO getMyPage(Long memberId, String posts) {
        Member member = memberRepository.findById(memberId).get();
        int titleCnt = titleRepository.countTrueColumnsForMember(memberId);

        List<Post> latestPostList = postList(memberId, posts);
        List<PostDTO> latestPostDTOList = latestPostList.stream()
                .map(this::getMyPostList)
                .collect(Collectors.toList());

        return MyPageDTO.builder()
                .memberId(memberId)
                .nickname(member.getNickname())
                .userImg(member.getUserImg())
                .memberTitle(member.getUserTitle())
                .titleCnt(titleCnt)
                .point(member.getPoint())
                .postList(latestPostDTOList)
                .build();
    }

    public List<Post> postList(Long memberId, String posts) {
        if (posts == null || posts.equals("written")) {
            // 작성한 최신 게시글 2개 조회
            List<Post> latestPosts = postRepository.findAllByMemberId(memberId, Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                    .limit(2)
                    .collect(Collectors.toList());
            return latestPosts;
        } else if (posts.equals("liked")) {
            // 좋아요한 최신 게시글 2개 조회
            List<Long> likedPostIds = likesRepository.findPostIdsByMemberId(memberId);
            List<Post> latestPosts = postRepository.findAllByIdIn(likedPostIds, Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                    .limit(2)
                    .collect(Collectors.toList());
            return latestPosts;
        } else if (posts.equals("voted")) {
            // 투표한 최신 게시글 2개 조회
            List<Long> votedPostIds = voteRepository.findPostIdsByMemberId(memberId);
            List<Post> latestPosts = postRepository.findAllByIdIn(votedPostIds, Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                    .limit(2)
                    .collect(Collectors.toList());
            return latestPosts;
        } else return null;
    }

    public PostDTO getMyPostList(Post post) {
        int participationCnt = post.getOptionsList().stream()
                .flatMap(option -> option.getVoteToOptionsList().stream())
                .map(voteToOptions -> voteToOptions.getVote().getMember())
                .distinct() // 멤버 중복 제거
                .collect(Collectors.counting()) // 참여자 수 계산
                .intValue();

        // 총 투표 수 계산
        long totalVoteCnt = post.getOptionsList().stream()
                .flatMap(option -> option.getVoteToOptionsList().stream())
                .count();

        List<OptionsDTO> optionsDTOList = post.getOptionsList().stream()
                .map(option -> {
                    // 해당 option의 투표 수 계산
                    long optionVoteCnt = option.getVoteToOptionsList().stream().count();

                    // 투표 결과 퍼센트 계산 (소수점 없음)
                    int votePercentage = (int) Math.round((optionVoteCnt * 100.0) / totalVoteCnt);

                    return new OptionsDTO(option.getBody(), option.getPhotoUrl(), votePercentage);
                })
                .collect(Collectors.toList());

        return PostDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .status(post.getStatus())
                .pluralVoting(post.getPluralVoting())
                .createdAt(formatCreatedAt(post, post.getCreatedAt()))
                .deadline(formatDeadline(post.getDeadline()))
                .optionsList(optionsDTOList)
                .participationCnt(participationCnt)
                .likesCnt(post.getLikesList().size())
                .commentsCnt(post.getCommentsList().size())
                .views(post.getViews())
                .build();
    }

    // 마감일 형식 수정하여 반환
    private String formatDeadline(Date dateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        return sdf.format(dateTime);
    }

    // 작성일 형식 수정하여 반환
    private String formatCreatedAt(Post post, LocalDateTime createdAt) {
        if (post.getCreatedAt().toLocalDate().isEqual(LocalDate.now())) {
            // 오늘이라면 HH:mm 시간만 표시
            return post.getCreatedAt().format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            // 오늘이 아니라면 MM-dd 형식으로 표시
            return post.getCreatedAt().format(DateTimeFormatter.ofPattern("MM-dd"));
        }
    }

}
