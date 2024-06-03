package KGUcapstone.OutDecision.domain.vote.repository;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT DISTINCT v.member.id FROM Vote v JOIN v.options o WHERE o.post.id = :postId")
    List<Long> findMemberIdsByPostId(Long postId);

    @Query("SELECT DISTINCT v.options.id FROM Vote v WHERE v.member = :member AND v.options.post = :post")
    List<Long> findOptionIdsByMemberAndPost(Member member, Post post);

    @Query("SELECT DISTINCT v.options.post.id, v.createdAt FROM Vote v WHERE v.member.id = :memberId ORDER BY v.createdAt DESC")
    List<Object[]> findDistinctPostIdsByMemberIdOrderByCreatedAtDesc(Long memberId);
}
