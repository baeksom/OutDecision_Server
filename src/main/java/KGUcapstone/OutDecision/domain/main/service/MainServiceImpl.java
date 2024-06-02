package KGUcapstone.OutDecision.domain.main.service;

import KGUcapstone.OutDecision.domain.main.dto.MainResponseDTO.PostListDTO;
import KGUcapstone.OutDecision.domain.post.converter.PostConverter;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.ranking.dto.RankingResponseDTO.RankingListDTO;
import KGUcapstone.OutDecision.domain.ranking.service.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainServiceImpl implements MainService{

    private final PostRepository postRepository;
    private final RankingService rankingService;
    private final PostConverter postConverter;

    @Override
    public PostListDTO getMain() {

        Pageable pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "bumpsTime"));
        Pageable pageable2 = PageRequest.of(0, 6, Sort.by(Sort.Direction.DESC, "deadline"));
        Pageable pageable3 = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "deadline"));
        // HOT 게시물 리스트
        List<PostDTO> hotPostDTOList = mapToDTO(postRepository.findByHotTrue(pageable));
        // 최신 게시물 리스트
        List<PostDTO> latestPostDTOList = mapToDTO(postRepository.findAll(pageable).getContent());
        // 투표 마감 게시물 리스트
        List<PostDTO> closedPostDTOList = mapToDTO(postRepository.findTop6ByStatusOrderByDeadlineDesc(Status.end, pageable2));
        // 투표 임박 게시글 리스트
        List<PostDTO> imminentPostDTOList = mapToDTO(postRepository.findTop6ByStatusOrderByDeadlineAsc(Status.progress, pageable3));

        RankingListDTO top10Rankings = rankingService.getTop10Rankings();

        return PostListDTO.builder()
                .hotPostList(hotPostDTOList)
                .latestPostList(latestPostDTOList)
                .closedPostList(closedPostDTOList)
                .imminentPostList(imminentPostDTOList)
                .rankingListDTO(top10Rankings)
                .build();
    }

    private List<PostDTO> mapToDTO(List<Post> posts) {
        return posts.stream()
                .map(postConverter::toPostDTO)
                .collect(Collectors.toList());
    }

}