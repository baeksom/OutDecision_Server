package KGUcapstone.OutDecision.domain.main.service;

import KGUcapstone.OutDecision.domain.main.dto.MainResponseReDTO.PostListReDTO;
import KGUcapstone.OutDecision.domain.post.converter.PostConverter;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.dto.PostsResponseDTO.PostDTO;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.post.service.PostsServiceImpl;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainServiceReImpl implements MainServiceRe{

    private final PostRepository postRepository;
    private final PostsServiceImpl postsService;
    private final FindMemberService findMemberService;
    private final PostConverter postConverter;

    @Override
    public PostListReDTO getMainRe() {
        Long memberId = findMemberService.findLoginMemberId();
        //추천 게시물 리스트
        List<PostDTO> recommendPostDTOList;
        if(memberId==0) {
            List<Post> posts = new ArrayList<>(postRepository.findAll()); // 수정 가능한 리스트로 변환
            Collections.shuffle(posts); // 리스트를 랜덤하게 섞음
            recommendPostDTOList = mapToDTO(posts.stream().limit(5).collect(Collectors.toList()));
        }
        else{
            List<Post> recommendPosts = postsService.recommendPost(memberId);
            recommendPostDTOList = mapToDTO(recommendPosts);
        }

        return PostListReDTO.builder()
                .recommendPostList(recommendPostDTOList)
                .build();
    }

    private List<PostDTO> mapToDTO(List<Post> posts) {
        return posts.stream()
                .map(postConverter::toPostDTO)
                .collect(Collectors.toList());
    }

}