package KGUcapstone.OutDecision.domain.post.converter;

import KGUcapstone.OutDecision.domain.likes.repository.LikesRepository;
import KGUcapstone.OutDecision.domain.notifications.repository.NotificationsRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.OptionsDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostListDTO;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.*;
import static KGUcapstone.OutDecision.global.util.DateTimeFormatUtil.formatCreatedAt;
import static KGUcapstone.OutDecision.global.util.DateTimeFormatUtil.formatDeadline;

@Component
@RequiredArgsConstructor
public class PostConverter {

    private final LikesRepository likesRepository;
    private final NotificationsRepository notificationsRepository;
    private final VoteRepository voteRepository;
    private final FindMemberService findMemberService;

    public PostDTO toPostDTO(Post post) {
        int participationCnt = post.getOptionsList().stream()
                .flatMap(option -> option.getVoteList().stream())
                .map(Vote::getMember)
                .distinct() // 멤버 중복 제거
                .toList().size();

        // 총 투표 수 계산
        long totalVoteCnt = post.getOptionsList().stream()
                .mapToLong(option -> option.getVoteList().size())
                .sum();

        List<OptionsDTO> optionsDtoList = post.getOptionsList().stream()
                .map(option -> {
                    // 해당 option의 투표 수 계산
                    long optionVoteCnt = option.getVoteList().size();

                    // 투표 결과 퍼센트 계산 (소수점 없음)
                    int votePercentage = (int) Math.round((optionVoteCnt * 100.0) / totalVoteCnt);

                    return OptionsDTO.builder()
                            .OptionId(option.getId())
                            .body(option.getBody())
                            .imgUrl(option.getPhotoUrl())
                            .votePercentage(votePercentage)
                            .build();
                })
                .toList();

        return PostDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .status(post.getStatus())
                .gender(post.getGender())
                .userId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .pluralVoting(post.getPluralVoting())
                .createdAt(formatCreatedAt(post.getCreatedAt()))
                .bumpsTime(formatCreatedAt(post.getBumpsTime()))
                .deadline(formatDeadline(post.getDeadline()))
                .participationCnt(participationCnt)
                .likesCnt(post.getLikes())
                .commentsCnt(post.getCommentsList().size())
                .views(post.getViews())
                .loginMemberPostInfoDTO(checkLoginPosts(post))
                .optionsList(optionsDtoList)
                .build();
    }

    public LoginMemberPostInfoDTO checkLoginPosts(Post post) {
        Optional<Member> optionalMember = findMemberService.findLoginMember();
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();

            Boolean isLiked = likesRepository.findByPostAndMember(post, member).isPresent();
            Boolean receiveAlert = notificationsRepository.findByPostAndMember(post, member).isPresent();
            List<Long> votedOptionIds = voteRepository.findOptionIdsByMemberAndPost(member, post);

            return LoginMemberPostInfoDTO.builder()
                    .isLiked(isLiked) // 좋아요 repo에서 (member, post) 값이 존재하는지
                    .receiveAlert(receiveAlert) // 알람 repo에서 (member, post) 값이 존재하는지
                    .votedOptionIds(votedOptionIds) // 투표 repo에서 (member, option.getPost()) 의 optionId 반환
                    .build();
        }
        else return null;
    }

    public PostListDTO toPostListDTO(Page<Post> postList) {
        List<PostDTO> postDTOList = postList.stream()
                .map(this::toPostDTO)
                .collect(Collectors.toList());

        return PostListDTO.builder()
                .postList(postDTOList)
                .listSize(postDTOList.size())
                .totalPage(postList.getTotalPages())
                .totalElements(postList.getTotalElements())
                .isFirst(postList.isFirst())
                .isLast(postList.isLast())
                .build();
    }
}