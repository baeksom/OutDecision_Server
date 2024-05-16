package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.likes.repository.LikesRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
import KGUcapstone.OutDecision.global.error.exception.handler.MemberHandler;
import KGUcapstone.OutDecision.global.error.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyActivityServiceImpl implements MyActivityService {

    private final PostRepository postRepository;
    private final LikesRepository likesRepository;
    private final VoteRepository voteRepository;
    private final FindMemberService findMemberService;

    // 작성한 글
    @Override
    public Page<Post> getMyPostListByStatus(Status status, Integer page) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Member member;
        // 로그인 체크
        if(memberOptional.isPresent()) member = memberOptional.get();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        Page<Post> postPage;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt"); // createdAt으로 내림차순 정렬
        if (status == null) {
            postPage = postRepository.findAllByMember(member, PageRequest.of(page, 10, sort));
        } else {
            postPage = postRepository.findAllByMemberAndStatus(member, status, PageRequest.of(page, 10, sort));
        }
        return postPage;
    }

    // 좋아요한 글
    @Override
    public Page<Post> getLikedPostListByStatus(Status status, Integer page) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Long memberId;
        // 로그인 체크
        if(memberOptional.isPresent()) memberId = memberOptional.get().getId();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        List<Long> likedPostIds = likesRepository.findPostIdsByMemberId(memberId);
        Page<Post> likedPosts;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt"); // createdAt으로 내림차순 정렬
        if (status == null) {
            likedPosts = postRepository.findAllByIdIn(likedPostIds, PageRequest.of(page, 10, sort));
        } else {
            likedPosts = postRepository.findAllByIdInAndStatus(likedPostIds, status, PageRequest.of(page, 10, sort));
        }
        return likedPosts;
    }

    // 투표한 글
    @Override
    public Page<Post> getVotedPostListByStatus(Status status, Integer page) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Long memberId;
        // 로그인 체크
        if(memberOptional.isPresent()) memberId = memberOptional.get().getId();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        List<Long> votedPostIds = voteRepository.findPostIdsByMemberId(memberId);
        Page<Post> votedPosts;
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt"); // createdAt으로 내림차순 정렬
        if (status == null) {
            votedPosts = postRepository.findAllByIdIn(votedPostIds, PageRequest.of(page, 10, sort));
        } else {
            votedPosts = postRepository.findAllByIdInAndStatus(votedPostIds, status, PageRequest.of(page, 10, sort));
        }
        return votedPosts;
    }

}
