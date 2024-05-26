package KGUcapstone.OutDecision.domain.comments.service;


import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.comments.dto.CommentsRequestDto;
import KGUcapstone.OutDecision.domain.comments.dto.CommentsResponseDto;
import KGUcapstone.OutDecision.domain.comments.repository.CommentsRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.global.error.exception.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentsService {

    private final CommentsRepository commentsRepository;
    private final PostRepository postRepository;
    private final FindMemberService findMemberService;


    @Transactional
    public Comments save(Long id, CommentsRequestDto dto) {

        Optional<Member> member = findMemberService.findLoginMember();

        if (member.isPresent()) {
            Post post = postRepository.findById(id).orElseThrow(() ->
                    new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다. " + id));

            Comments comment = dto.toEntity(member.get(), post);
            commentsRepository.save(comment);

            return comment;
        } else {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }
    }


    @Transactional
    public void delete(Long postsId, Long commentsId) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Long memberId;
        // 로그인 체크
        if(memberOptional.isPresent()) memberId = memberOptional.get().getId();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        Comments comments = commentsRepository.findByPostIdAndId(postsId, commentsId).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글이 존재하지 않습니다. commentsId=" + commentsId));

        if (!memberId.equals(comments.getMember().getId())) throw new IllegalArgumentException("Bad Request");

        commentsRepository.delete(comments);
    }


}