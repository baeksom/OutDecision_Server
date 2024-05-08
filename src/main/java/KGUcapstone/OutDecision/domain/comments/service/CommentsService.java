package KGUcapstone.OutDecision.domain.comments.service;


import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.comments.dto.CommentsRequestDto;
import KGUcapstone.OutDecision.domain.comments.dto.CommentsResponseDto;
import KGUcapstone.OutDecision.domain.comments.repository.CommentsRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;


    @Transactional
    public Long save(String nickname, Long id, CommentsRequestDto dto) {

        Member member = memberRepository.findByNickname(nickname);
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + id));

        dto.setMember(member);
        dto.setPost(post);

        Comments comment = dto.toEntity();
        commentsRepository.save(comment);

        return comment.getId();

    }


    @Transactional(readOnly = true)
    public List<CommentsResponseDto> findAll(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당게시글이 존재하지 않습니다." + id));
        List<Comments> comments = post.getCommentsList();
        return comments.stream().map(CommentsResponseDto::new).collect(Collectors.toList());

    }

    @Transactional
    public void delete(Long postsId, Long id) {
        Comments comments = commentsRepository.findByPostIdAndId(postsId, id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. id=" + id));

        commentsRepository.delete(comments);
    }


}