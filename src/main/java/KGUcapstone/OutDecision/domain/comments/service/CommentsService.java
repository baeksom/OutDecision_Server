package KGUcapstone.OutDecision.domain.comments.service;


import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.comments.dto.CommentsRequestDto;
import KGUcapstone.OutDecision.domain.comments.repository.CommentsRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentsService {

    private final CommentsService commentsService;
//    private final MemberRepository memberRepository;
//    private final PostRepository postRepository;

    /* 등록 */
    @Transactional
    public Long commentsSave(String nickname, Long id, CommentsRequestDto dto) {

//        Member member = MemberRepository.findByNickname(nickname);
//        Post post = PostRepository.findById(id).orElseThrow(() ->
//                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + id));

//        dto.setMember(member);
//        dto.setPost(post);

        Comments comment = dto.toEntity();
        CommentsRepository.save(comment);

        return comment.getId();

    }

    /* 삭제 */
    @Transactional
    public void delete(Long postsId, Long id) {
        Comments comments = CommentsRepository.findByPostsIdAndId(postsId, id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + id));

        CommentsRepository.delete(comments);
    }


}