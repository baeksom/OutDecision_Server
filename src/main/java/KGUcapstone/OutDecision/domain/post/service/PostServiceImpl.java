package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.options.domain.Options;
import KGUcapstone.OutDecision.domain.options.repository.OptionsRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
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
import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
import KGUcapstone.OutDecision.global.error.exception.handler.PostHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static KGUcapstone.OutDecision.global.util.DateTimeFormatUtil.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final OptionsRepository optionsRepository;
    private final VoteRepository voteRepository;
    private final S3Service s3Service;

    /* 등록 */
    @Override
    public boolean uploadPost(UploadPostDTO request, List<String> optionNames, List<MultipartFile> optionImages) {
        Long memberId = 2024L;
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member를 찾을 수 없습니다."));

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(Category.fromValue(request.getCategory()))
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

        List<Options> optionsList = new ArrayList<>();
        if (optionNames != null && optionImages != null && optionNames.size() == optionImages.size()) {
            for (int i = 0; i < optionNames.size(); i++) {
                String optionName = optionNames.get(i);
                MultipartFile imageFile = optionImages.get(i);
                String photoUrl = "";
                if (imageFile != null && !imageFile.isEmpty()) {
                    // 이미지 파일 업로드 후 URL 획득
                    photoUrl = s3Service.uploadFile(imageFile, "options");
                }
                // 옵션 엔터티 생성
                Options newOption = Options.builder()
                        .body(optionName)
                        .photoUrl(photoUrl)
                        .post(post)
                        .build();
//                // 옵션을 저장소에 저장
                optionsRepository.save(newOption);
                optionsList.add(newOption); // 옵션 리스트에 새로운 옵션 추가
            }
        }

        // 옵션 리스트를 포스트에 설정
        post.setOptionsList(optionsList);
        postRepository.save(post);

        return true;
    }




    /* 조회 */
    @Override
    public PostDTO viewPost(Long postId) {
        Long memberId = 2024L;
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
                .bumps(memberId.equals(post.getMember().getId()) ? post.getMember().getBumps() : null)
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
    @Override
    public boolean updatePost(Long postId, UploadPostDTO request, List<String> optionNames, List<MultipartFile> optionImages) {
        Long memberId = 2024L;
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        if (!memberId.equals(post.getMember().getId())) return false;

        // 기존 옵션 및 이미지 삭제
        for (Options option : post.getOptionsList()) {
            if (option.getPhotoUrl() != null) {
                // S3에서 이미지 삭제
                s3Service.deleteImage(option.getPhotoUrl());
            }
            optionsRepository.delete(option);
        }
        post.getOptionsList().clear(); // 옵션 리스트 초기화
        postRepository.save(post); // 변경사항 저장

        // 새로운 옵션 추가
        List<Options> optionsList = new ArrayList<>();
        if (optionNames != null && optionImages != null && optionNames.size() == optionImages.size()) {
            for (int i = 0; i < optionNames.size(); i++) {
                String optionName = optionNames.get(i);
                MultipartFile imageFile = optionImages.get(i);
                String photoUrl = "";
                if (imageFile != null && !imageFile.isEmpty()) {
                    photoUrl = s3Service.uploadFile(imageFile, "options");
                }
                Options newOption = Options.builder()
                        .body(optionName)
                        .photoUrl(photoUrl)
                        .post(post)
                        .build();
                optionsRepository.save(newOption); // 옵션을 저장소에 저장
                optionsList.add(newOption); // 옵션 리스트에 새로운 옵션 추가
            }
        }
        // 포스트에 새로운 옵션 리스트 설정
        post.setOptionsList(optionsList);
        post.updatePost(request.getTitle(), request.getContent(), Category.fromValue(request.getCategory()),
                parseStringToDate(request.getDeadline()), request.isPluralVoting(), request.getGender());
        postRepository.save(post);

        return true;
    }


    /* 삭제 */
    @Override
    public boolean deletePost(Long postId) {
        Long memberId = 2024L;
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        if (!memberId.equals(post.getMember().getId())) {
            return false;
        }

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

    // 핫 게시글 변경
    @Override
    public void turnsHot (Post post) {
        List<Long> votes = voteRepository.findMemberIdsByPostId(post.getId());
        if (!post.getHot() && post.getLikes()>=10 && votes.size() >= 20) {
            // 좋아요가 10 이상, 투표한 사람이 20 이상일 경우에 핫 게시글
            post.updateHot(true);
        }
    }
}
