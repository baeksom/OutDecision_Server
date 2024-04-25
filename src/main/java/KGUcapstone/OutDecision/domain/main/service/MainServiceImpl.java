package KGUcapstone.OutDecision.domain.main.service;

import KGUcapstone.OutDecision.domain.main.dto.MainResponseDTO.PostListDTO;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.OptionsDTO;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.PostDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class MainServiceImpl implements MainService{

    private final PostRepository postRepository;

    @Override
    public PostListDTO getMain() {

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

        // HOT 게시물 리스트
        List<PostDTO> hotPostDTOList = mapToDTO(postRepository.findByHotTrue(pageable));
        // 최신 게시물 리스트
        List<PostDTO> latestPostDTOList = mapToDTO(postRepository.findAll(pageable).getContent());
        // 투표 마감 게시물 리스트
        List<PostDTO> closedPostDTOList = mapToDTO(postRepository.findTopNByStatusOrderByCreatedAtDesc(Status.CLOSING, pageable));

        return PostListDTO.builder()
                .hotPostList(hotPostDTOList)
                .latestPostList(latestPostDTOList)
                .closedPostList(closedPostDTOList)
                .build();
    }

    private List<PostDTO> mapToDTO(List<Post> posts) {
        return posts.stream()
                .map(this::post)
                .collect(Collectors.toList());
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
}
