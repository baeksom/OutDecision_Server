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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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
        Sort sort = Sort.by(Sort.Direction.DESC, "bumpsTime"); // bumpsTime으로 내림차순 정렬
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
        if (memberOptional.isPresent()) memberId = memberOptional.get().getId();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        List<Long> likedPostIds = likesRepository.findPostIdsByMemberIdOrderByCreatedAtDesc(memberId);
        List<Post> likedPosts = likedPostIds.stream()
                .map(postRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        // 상태 필터링
        if (status != null) {
            likedPosts = likedPosts.stream()
                    .filter(post -> post.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        // 페이지 처리를 수동으로 수행
        int start = Math.min(page * 10, likedPosts.size());
        int end = Math.min((page + 1) * 10, likedPosts.size());
        List<Post> subList = likedPosts.subList(start, end);
        return new PageImpl<>(subList, PageRequest.of(page, 10), likedPosts.size());
    }

    // 투표한 글
    @Override
    public Page<Post> getVotedPostListByStatus(Status status, Integer page) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();
        Long memberId;
        // 로그인 체크
        if (memberOptional.isPresent()) memberId = memberOptional.get().getId();
        else throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);

        List<Object[]> votedPostData = voteRepository.findDistinctPostIdsByMemberIdOrderByCreatedAtDesc(memberId);

        // 게시글 ID만 추출하여 리스트에 저장
        List<Long> votedPostIds = votedPostData.stream()
                .map(data -> (Long) data[0])
                .collect(Collectors.toList());

        // 페이지 처리를 수동으로 수행
        int start = page * 10;
        int end = Math.min((page + 1) * 10, votedPostIds.size());

        List<Long> subList = votedPostIds.subList(start, end);
        List<Post> votedPosts = subList.stream()
                .map(postRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        // 상태 필터링
        if (status != null) {
            votedPosts = votedPosts.stream()
                    .filter(post -> post.getStatus().equals(status))
                    .collect(Collectors.toList());
        }

        return new PageImpl<>(votedPosts, PageRequest.of(page, 10), votedPostIds.size());
    }

}
