package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.dto.PostRequestDto;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDto;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    private final PostRepository postRepository;

    /* 등록 */
    @Transactional
    public Long save(PostRequestDto dto) {
        //Member의 id 가져와서 Post의 member_id에 저장필요 => 어떤 사람이 했는지 알기위해??
        Post post = dto.toEntity();
        return postRepository.save(post).getId();
    }

    /* 조회 */
    @Transactional
    public PostResponseDto findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()
                ->new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id) );
        return new PostResponseDto(post);
    }

    /* 수정 */
    @Transactional
    public void update(Long id, PostRequestDto dto) {
        Post post = postRepository.findById(id).orElseThrow(()
                ->new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id) );
        post.update(dto.getTitle(), dto.getContent());
    }

    /* 삭제 */
    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()
                ->new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id) );
        postRepository.delete(post);
    }

}
