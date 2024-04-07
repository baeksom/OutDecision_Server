package KGUcapstone.OutDecision.domain.user.service;

import KGUcapstone.OutDecision.domain.options.domain.Options;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.MyPostDTO;
import KGUcapstone.OutDecision.domain.user.dto.ActivityResponseDTO.MyPostListDTO;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPostServiceImpl implements MyPostService{

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Override
    public Page<Post> getMyPostListByStatus(Long memberId, Status status, Integer page) {
        Member member = memberRepository.findById(memberId).get();
        Page<Post> postPage;
        if (status == null) {
            postPage = postRepository.findAllByMember(member, PageRequest.of(page, 10));
        } else {
            postPage = postRepository.findAllByMemberAndStatus(member, status, PageRequest.of(page, 10));
        }
        return postPage;
    }

    @Override
    public MyPostDTO myPostDTO(Post post) {
        List<String> optionsListDTOS = post.getOptionsList().stream()
                .map(Options::getBody)
                .collect(Collectors.toList());

        return MyPostDTO.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .category(post.getCategory())
                .deadline(post.getDeadline())
                .status(post.getStatus())
                .optionsList(optionsListDTOS)
                .commentsCnt(post.getCommentsList().size())
                .build();
    }

    @Override
    public MyPostListDTO myPostListDTO(Page<Post> postList) {
        List<MyPostDTO> myPostDTOList = postList.stream()
                .map(this::myPostDTO)
                .collect(Collectors.toList());

        return MyPostListDTO.builder()
                .isLast(postList.isLast())
                .isFirst(postList.isFirst())
                .totalPage(postList.getTotalPages())
                .totalElements(postList.getTotalElements())
                .listSize(myPostDTOList.size())
                .myPostList(myPostDTOList)
                .build();
    }
}
