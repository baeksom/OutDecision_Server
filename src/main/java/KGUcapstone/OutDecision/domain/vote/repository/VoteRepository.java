package KGUcapstone.OutDecision.domain.vote.repository;

import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT DISTINCT v.options.post.id FROM Vote v JOIN v.options o WHERE v.member.id = :memberId")
    List<Long> findPostIdsByMemberId(Long memberId);

    @Query("SELECT DISTINCT v.member.id FROM Vote v JOIN v.options o WHERE o.post.id = :postId")
    List<Long> findMemberIdsByPostId(Long postId);
}
