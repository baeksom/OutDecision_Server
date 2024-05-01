package KGUcapstone.OutDecision.domain.post.service;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.dto.PostRequestDto;
import KGUcapstone.OutDecision.domain.post.dto.PostResponseDto;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    /* 등록 */
    @Transactional
    public boolean save(PostRequestDto dto) {
//        Member member = memberRepository.findByNickname(nickname);

        Post post = dto.toEntity();
        postRepository.save(post);

        return true;
    }

    /* 조회 */
    @Transactional
    public PostResponseDto get(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()
                ->new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id) );
        post.incrementViews();
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    /* 수정 */
    @Transactional
    public boolean update(Long id, PostRequestDto dto) {
        Post post = postRepository.findById(id).orElseThrow(()
                ->new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id) );
        post.update(dto.getTitle(), dto.getContent());
        postRepository.save(post);

        return true;
    }

    /* 삭제 */
    @Transactional
    public boolean delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()
                ->new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id = " + id) );
        postRepository.delete(post);
        return true;
    }

}
