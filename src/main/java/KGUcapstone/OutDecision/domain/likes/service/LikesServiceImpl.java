package KGUcapstone.OutDecision.domain.likes.service;

import KGUcapstone.OutDecision.domain.likes.domain.Likes;
import KGUcapstone.OutDecision.domain.likes.repository.LikesRepository;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.repository.PostRepository;
import KGUcapstone.OutDecision.domain.post.service.PostService;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.user.service.FindMemberService;
import KGUcapstone.OutDecision.domain.user.service.mypage.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LikesServiceImpl implements LikesService{

    private final FindMemberService findMemberService;
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final TitleService titleService;
    private final PostService postService;

    public Long addLikes(Long postId) {
        Optional<Member> memberOptional = findMemberService.findLoginMember();

        if (memberOptional.isPresent()) {
            Member member = memberOptional.get();
            Post post = postRepository.findById(postId).orElseThrow(() ->
                    new IllegalArgumentException("게시물이 존재하지 않습니다."));

            // 게시물 좋아요수 +1
            post.updateLikes(post.getLikes() + 1);

            // 좋아요 저장
            Likes likes = toLikesEntity(member, post);
            likesRepository.save(likes);

            // 핫 게시글 가능 여부 확인
            postService.turnsHot(post);

            // 칭호 획득 가능 여부 확인
            titleService.memberGetTitle(post, member);
            return likes.getId();
        } else {
            throw new RuntimeException("User Not Found");
        }
    }

    private Likes toLikesEntity (Member member, Post post) {
        return Likes.builder()
                .member(member)
                .post(post)
                .build();
    }
}
