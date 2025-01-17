package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.notifications.domain.Notifications;
import KGUcapstone.OutDecision.domain.notifications.repository.NotificationsRepository;
import KGUcapstone.OutDecision.domain.options.domain.Options;
import KGUcapstone.OutDecision.domain.options.repository.OptionsRepository;
import KGUcapstone.OutDecision.domain.post.converter.PostConverter;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.dto.PostRequestDTO.UploadPostDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.CommentsDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.CommentsListDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.OptionsDTO;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.title.domain.Missions;
import KGUcapstone.OutDecision.domain.title.repository.MissionsRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.domain.MemberView;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.repository.MemberViewRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.user.service.S3Service;
import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
import KGUcapstone.OutDecision.global.error.exception.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.exception.handler.PostHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;

import static KGUcapstone.OutDecision.global.util.DateTimeFormatUtil.*;

@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final OptionsRepository optionsRepository;
    private final NotificationsRepository notificationsRepository;
    private final VoteRepository voteRepository;
    private final FindMemberService findMemberService;
    private final MemberViewRepository memberViewRepository;
    private final MissionsRepository missionsRepository;

    private final S3Service s3Service;
    private final PostConverter postConverter;

    /* 등록 */
    @Override
    public boolean uploadPost(UploadPostDTO request, List<String> optionNames, List<MultipartFile> optionImages) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Member member;
        // 로그인 체크
        if(memberOptional.isPresent()) member = memberOptional.get();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        Post post = Post.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .category(Category.fromValue(request.getCategory()))
                .deadline(parseStringToDate(request.getDeadline()))
                .pluralVoting(request.isPluralVoting())
                .gender(request.getGender())
                .member(member)
                .status(Status.progress)
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
                // 옵션을 저장소에 저장
                optionsRepository.save(newOption);
                optionsList.add(newOption); // 옵션 리스트에 새로운 옵션 추가
            }
        }

        // 옵션 리스트를 포스트에 설정
        post.setOptionsList(optionsList);
        postRepository.save(post);

        Notifications notifications = Notifications.builder()
                .member(member)
                .post(post)
                .build();
        notificationsRepository.save(notifications);

        return true;
    }

    /* 조회 */
    @Override
    public PostDTO viewPost(Long postId) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Long currentMemberId;
        // 로그인 체크
        if(memberOptional.isPresent()) currentMemberId = memberOptional.get().getId();
        else currentMemberId = 0L;

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        post.incrementViews();
        postRepository.save(post);

        if (!currentMemberId.equals(0L)) {
            MemberView memberView = memberViewRepository.findByMemberIdAndCategory(currentMemberId, post.getCategory());
            if (memberView != null) {
                memberView.setViews(memberView.getViews() + 1);
                memberViewRepository.save(memberView);
            } else {
                Member member = memberOptional.get();
                memberView = MemberView.builder()
                        .member(member)
                        .category(post.getCategory())
                        .views(1)
                        .build();
                memberViewRepository.save(memberView);
            }
        }


        List<CommentsDTO> commentsList = post.getCommentsList().stream()
                .sorted(Comparator.comparing(Comments::getCreatedAt).reversed())
                .map(comments -> CommentsDTO.builder()
                        .commentsId(comments.getId())
                        .memberId(comments.getMember().getId())
                        .nickname(comments.getMember().getNickname())
                        .profileUrl(comments.getMember().getUserImg())
                        .memberTitle(comments.getMember().getUserTitle())
                        .body(comments.getBody())
                        .createdAt(formatCreatedAt2(comments.getCreatedAt()))
                        .isOwn(comments.getMember().getId().equals(currentMemberId))
                        .build())
                .toList();

        CommentsListDTO commentsListDTO = CommentsListDTO.builder()
                .commentsDTOList(commentsList)
                .listSize(commentsList.size())
                .build();

        Member postMember = post.getMember();

        return PostDTO.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .status(post.getStatus())
                .gender(post.getGender())
                .userId(postMember.getId())
                .nickname(postMember.getNickname())
                .memberTitle(postMember.getUserTitle())
                .profileUrl(postMember.getUserImg())
                .bumps(currentMemberId.equals(postMember.getId()) ? postMember.getBumps() : null)
                .pluralVoting(post.getPluralVoting())
                .createdAt(formatCreatedAt2(post.getCreatedAt()))
                .bumpsTime(formatCreatedAt(post.getBumpsTime()))
                .deadline(formatDeadline(post.getDeadline()))
                .participationCnt(getParticipationCnt(post))
                .likesCnt(post.getLikes())
                .views(post.getViews())
                .optionsList(optionList(post))
                .commentsList(commentsListDTO)
                .loginMemberPostInfoDTOList(postConverter.checkLoginPosts(post))
                .build();
    }

    /* 수정 */
    @Override
    public boolean updatePost(Long postId, UploadPostDTO request, List<String> optionNames,
                              List<MultipartFile> optionImages, List<String> originImages) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Long memberId;
        // 로그인 체크
        if(memberOptional.isPresent()) memberId = memberOptional.get().getId();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));
        if (!memberId.equals(post.getMember().getId())) return false;

        // 기존 옵션 이미지 리스트를 수집
        List<String> originOptionImgList = post.getOptionsList().stream()
                .map(Options::getPhotoUrl)
                .filter(url -> url != null && !url.isEmpty())
                .toList();

        // 기존 옵션 삭제
        optionsRepository.deleteAll(post.getOptionsList());
        post.getOptionsList().clear();

        // 새로운 옵션 추가
        List<Options> optionsList = new ArrayList<>();
        if (optionNames != null && optionImages != null && optionNames.size() == optionImages.size()) {
            for (int i = 0; i < optionNames.size(); i++) {
                String optionName = optionNames.get(i);
                String photoUrl = "";

                // 새로운 이미지 업로드 또는 기존 이미지 사용
                if (!optionImages.get(i).isEmpty()) {
                    photoUrl = s3Service.uploadFile(optionImages.get(i), "options");
                } else if (!originImages.get(i).isEmpty()) {
                    photoUrl = originImages.get(i);
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

        // S3에서 사용되지 않는 기존 이미지 삭제
        for (String originImageUrl : originOptionImgList) {
            boolean existsInOptionsList = optionsList.stream()
                    .anyMatch(option -> originImageUrl.equals(option.getPhotoUrl()));
            if (!existsInOptionsList && originImageUrl != null && !originImageUrl.isEmpty()) {
                s3Service.deleteImg(originImageUrl);
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
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Long memberId;
        // 로그인 체크
        if(memberOptional.isPresent()) memberId = memberOptional.get().getId();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostHandler(ErrorStatus.POST_NOT_FOUND));

        if (!memberId.equals(post.getMember().getId())) {
            return false;
        }

        for (Options options:post.getOptionsList()) {
            if (!options.getPhotoUrl().isEmpty()) {
                s3Service.deleteImg(options.getPhotoUrl());
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
                            .optionId(option.getId())
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
            Missions missions = missionsRepository.findAllByMemberId(post.getMember().getId());
            switch (post.getCategory()){
                case food:
                    missions.setFoodie_cnt(missions.getFoodie_cnt()+1);
                    break;
                case love:
                    missions.setRomantist_cnt(missions.getRomantist_cnt()+1);
                    break;
                case work:
                    missions.setCeo_cnt(missions.getCeo_cnt()+1);
                    break;
                case hobby:
                    missions.setHobbyist_cnt(missions.getHobbyist_cnt()+1);
                    break;
                case travel:
                    missions.setTraveler_cnt(missions.getTraveler_cnt()+1);
                    break;
                case fashion:
                    missions.setFashionista_cnt(missions.getFashionista_cnt()+1);
                    break;
                case other:
                    break;
            }

            // 포인트 +300 적립
            post.getMember().updatePoint(post.getMember().getPoint()+300);
        }
    }

    // 게시글 끌어올리기
    public boolean topPost(Long postId) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Member member;
        // 로그인 체크
        if(memberOptional.isPresent()) member = memberOptional.get();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        Post post = postRepository.findById(postId).orElseThrow(() ->
                new PostHandler(ErrorStatus.POST_NOT_FOUND));

        if (!member.getId().equals(post.getMember().getId())) { // 게시글을 작성한 유저 맞는지 확인
            System.out.println("작성자가 아닙니다.");
            return false;
        }

        if (post.getStatus().equals(Status.end)){  // 투표 중인 게시글만
            System.out.println("투표가 마감 되었습니다.");
            return false;
        }

        if(member.getBumps() == 0) { // 끌올 1개이상
            System.out.println("끌어올리기 횟수가 부족합니다.");
            return false;
        }

        int bumpCount = member.getBumps() - 1;
        member.updateBumps(bumpCount);
        memberRepository.save(member);

        post.updateBumpsTime();
        postRepository.save(post);

        return true;
    }
}
