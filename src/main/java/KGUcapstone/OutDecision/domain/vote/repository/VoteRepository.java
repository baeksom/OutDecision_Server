package KGUcapstone.OutDecision.domain.vote.repository;

import KGUcapstone.OutDecision.domain.options.domain.Options;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT DISTINCT v.options.post.id FROM Vote v JOIN v.options o WHERE v.member.id = :memberId")
    List<Long> findPostIdsByMemberId(Long memberId);

    @Query("SELECT DISTINCT v.member.id FROM Vote v JOIN v.options o WHERE o.post.id = :postId")
    List<Long> findMemberIdsByPostId(Long postId);

    @Query("SELECT DISTINCT v.options.id FROM Vote v WHERE v.member = :member AND v.options.post = :post")
    List<Long> findOptionIdsByMemberAndPost(Member member, Post post);
}
