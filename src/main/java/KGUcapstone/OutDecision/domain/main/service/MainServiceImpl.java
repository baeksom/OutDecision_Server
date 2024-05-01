package KGUcapstone.OutDecision.domain.main.service;

import KGUcapstone.OutDecision.domain.main.dto.MainResponseDTO.PostListDTO;
import KGUcapstone.OutDecision.domain.post.converter.PostConverter;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
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

    @Override
    public PostListDTO getMain() {

        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

        // HOT 게시물 리스트
        List<PostDTO> hotPostDTOList = mapToDTO(postRepository.findByHotTrue(pageable));
        // 최신 게시물 리스트
        List<PostDTO> latestPostDTOList = mapToDTO(postRepository.findAll(pageable).getContent());
        // 투표 마감 게시물 리스트
        List<PostDTO> closedPostDTOList = mapToDTO(postRepository.findTop5ByStatusOrderByCreatedAtDesc(Status.CLOSING, pageable));

        return PostListDTO.builder()
                .hotPostList(hotPostDTOList)
                .latestPostList(latestPostDTOList)
                .closedPostList(closedPostDTOList)
                .build();
    }

    private List<PostDTO> mapToDTO(List<Post> posts) {
        return posts.stream()
                .map(PostConverter::toPostDTO)
                .collect(Collectors.toList());
    }

}
