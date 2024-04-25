package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.likes.repository.LikesRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.OptionsDTO;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.PostListDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
@Transactional(readOnly = true)
public class MyActivityServiceImpl implements MyActivityService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final VoteRepository voteRepository;

    // 작성한 글
    @Override
    public Page<Post> getMyPostListByStatus(Long memberId, Status status, Integer page) {
        Member member = memberRepository.findById(memberId).get();
        Page<Post> postPage;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt"); // createdAt으로 내림차순 정렬
        if (status == null) {
            postPage = postRepository.findAllByMember(member, PageRequest.of(page, 10, sort));
        } else {
            postPage = postRepository.findAllByMemberAndStatus(member, status, PageRequest.of(page, 10, sort));
        }
        return postPage;
    }

    // 좋아요한 글
    @Override
    public Page<Post> getLikedPostListByStatus(Long memberId, Status status, Integer page) {
        List<Long> likedPostIds = likesRepository.findPostIdsByMemberId(memberId);
        Page<Post> likedPosts;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt"); // createdAt으로 내림차순 정렬
        if (status == null) {
            likedPosts = postRepository.findAllByIdIn(likedPostIds, PageRequest.of(page, 10, sort));
        } else {
            likedPosts = postRepository.findAllByIdInAndStatus(likedPostIds, status, PageRequest.of(page, 10, sort));
        }
        return likedPosts;
    }

    // 투표한 글
    @Override
    public Page<Post> getVotedPostListByStatus(Long memberId, Status status, Integer page) {
        List<Long> votedPostIds = voteRepository.findPostIdsByMemberId(memberId);
        Page<Post> votedPosts;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt"); // createdAt으로 내림차순 정렬
        if (status == null) {
            votedPosts = postRepository.findAllByIdIn(votedPostIds, PageRequest.of(page, 10, sort));
        } else {
            votedPosts = postRepository.findAllByIdInAndStatus(votedPostIds, status, PageRequest.of(page, 10, sort));
        }
        return votedPosts;
    }

    // 게시글
    @Override
    public PostDTO post(Post post) {
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
                .content(post.getContent())
                .category(post.getCategory())
                .status(post.getStatus())
                .pluralVoting(post.getPluralVoting())
                .createdAt(formatCreatedAt(post, post.getCreatedAt()))
                .deadline(formatDeadline(post.getDeadline()))
                .optionsList(optionsDTOList)
                .participationCnt(participationCnt)
                .likesCnt(post.getLikes())
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

    // 게시글 list
    @Override
    public PostListDTO postList(Page<Post> postList) {
        List<PostDTO> postDTOList = postList.stream()
                .map(this::post)
                .collect(Collectors.toList());

        return PostListDTO.builder()
                .isLast(postList.isLast())
                .isFirst(postList.isFirst())
                .totalPage(postList.getTotalPages())
                .totalElements(postList.getTotalElements())
                .listSize(postDTOList.size())
                .postList(postDTOList)
                .build();
    }

}
