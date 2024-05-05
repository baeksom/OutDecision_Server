package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.dto.PostRequestDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.CommentsDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.CommentsListDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.OptionsDTO;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import KGUcapstone.OutDecision.global.error.exception.handler.PostHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static KGUcapstone.OutDecision.global.util.DateTimeFormatUtil.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;

    /* 등록 */
    @Transactional
    public Long save(PostRequestDTO dto) {
        //Member의 id 가져와서 Post의 member_id에 저장필요 => 어떤 사람이 했는지 알기위해??
        Post post = dto.toEntity();
        postRepository.save(post);
        return post.getId();
    }

    /* 조회 */
    @Override
    public PostDTO viewPost(Long postId) {
        Post post = postRepository.findById(postId).get();
        post.incrementViews();
        postRepository.save(post);
        List<CommentsDTO> commentsList = post.getCommentsList().stream()
                .map(comments -> {
                    return CommentsDTO.builder()
                            .memberId(comments.getMember().getId())
                            .nickname(comments.getMember().getNickname())
                            .body(comments.getBody())
                            .createdAt(formatCreatedAt2(comments.getCreatedAt()))
                            .build();
                }).toList();
        CommentsListDTO commentsListDTO = CommentsListDTO.builder()
                .commentsDTOList(commentsList)
                .listSize(commentsList.size())
                .build();

        return PostDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .status(post.getStatus())
                .gender(post.getGender())
                .userId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .pluralVoting(post.getPluralVoting())
                .createdAt(formatCreatedAt(post.getCreatedAt()))
                .deadline(formatDeadline(post.getDeadline()))
                .participationCnt(getParticipationCnt(post))
                .likesCnt(post.getLikes())
                .views(post.getViews())
                .optionsList(optionList(post))
                .commentsList(commentsListDTO)
                .build();
    }

    /* 수정 */
    @Transactional
    public Long update(Long id, PostRequestDTO dto) {
        Post post = postRepository.findById(id).orElseThrow(()
                ->new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id) );
        post.update(dto.getTitle(), dto.getContent());

        return id;
    }

    /* 삭제 */
    @Override
    public boolean deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        postRepository.delete(post);
        return true;
    }

    // 참여자 수 계산
    private int getParticipationCnt(Post post) {
        int participationCnt = post.getOptionsList().stream()
                .flatMap(option -> option.getVoteList().stream())
                .map(Vote::getMember)
                .distinct() // 멤버 중복 제거
                .toList().size();
        return participationCnt;
    }

    // 옵션 리스트
    private List<OptionsDTO> optionList(Post post) {
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

        return optionsDtoList;
    }

}
