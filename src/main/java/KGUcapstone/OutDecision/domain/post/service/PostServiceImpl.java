package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.options.domain.Options;
import KGUcapstone.OutDecision.domain.options.repository.OptionsRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.dto.PostRequestDTO.UploadPostDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.CommentsDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.CommentsListDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.OptionsDTO;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.S3Service;
import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import KGUcapstone.OutDecision.global.error.exception.handler.PostHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static KGUcapstone.OutDecision.global.util.DateTimeFormatUtil.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final OptionsRepository optionsRepository;
    private final S3Service s3Service;

    /* 등록 */
    @Override
    public boolean uploadPost(UploadPostDTO request, List<String> optionNames, List<MultipartFile> optionImages){
        Long memberId = 2024L;
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member를 찾을 수 없습니다."));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .deadline(parseStringToDate(request.getDeadline()))
                .pluralVoting(request.isPluralVoting())
                .gender(request.getGender())
                .member(member)
                .status(Status.VOTING)
                .bumpsTime(LocalDateTime.now())
                .likes(0)
                .views(0)
                .hot(false)
                .build();
        postRepository.save(post);
        List<String> optionImgsList = new ArrayList<>();
        if (optionNames == null) return false;
        if (optionImages != null) optionImgsList = s3Service.uploadFiles(optionImages, "options");

        List<Options> optionsList = new ArrayList<>();
        for (int i = 0; i < optionNames.size(); i++) {
            Options options = Options.builder()
                    .body(optionNames.get(i))
                    .photoUrl((optionImgsList != null && i < optionImgsList.size()) ? optionImgsList.get(i) : null)
                    .post(post)
                    .build();
            optionsRepository.save(options);
        }

        post.setOptionsList(optionsList);
        postRepository.save(post);

        return true;
    }



    /* 조회 */
    @Override
    public PostDTO viewPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
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
//    @Transactional
//    public Long update(Long id, PostRequestDTO dto) {
//        Post post = postRepository.findById(id).orElseThrow(()
//                ->new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id) );
//        post.update(dto.getTitle(), dto.getContent());
//
//        return id;
//    }

    /* 삭제 */
    @Override
    public boolean deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        for (Options options:post.getOptionsList()) {
            if (options.getPhotoUrl() != null) {
                s3Service.deleteImage(options.getPhotoUrl());
            }
        }
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
