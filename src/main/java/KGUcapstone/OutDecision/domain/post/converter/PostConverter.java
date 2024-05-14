package KGUcapstone.OutDecision.domain.post.converter;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.OptionsDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostListDTO;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

import static KGUcapstone.OutDecision.global.util.DateTimeFormatUtil.formatCreatedAt;
import static KGUcapstone.OutDecision.global.util.DateTimeFormatUtil.formatDeadline;

public class PostConverter {

    public static PostDTO toPostDTO(Post post) {
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
                .optionsList(optionsDtoList)
                .build();
    }

    public static PostListDTO toPostListDTO(Page<Post> postList) {
        List<PostDTO> postDTOList = postList.stream()
                .map(PostConverter::toPostDTO)
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