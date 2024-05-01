package KGUcapstone.OutDecision.domain.user.service.mypage;

import KGUcapstone.OutDecision.domain.likes.repository.LikesRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.repository.MemberRepository;
import KGUcapstone.OutDecision.domain.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyActivityServiceImpl implements MyActivityService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final LikesRepository likesRepository;
    private final VoteRepository voteRepository;

    // 작성한 글
    @Override
    public Page<Post> getMyPostListByStatus(Long memberId, Status status, Integer page) {
        Member member = memberRepository.findById(memberId).get();
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
    public Page<Post> getLikedPostListByStatus(Long memberId, Status status, Integer page) {
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
    public Page<Post> getVotedPostListByStatus(Long memberId, Status status, Integer page) {
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
