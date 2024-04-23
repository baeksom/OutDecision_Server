package KGUcapstone.OutDecision.domain.vote.repository;

import KGUcapstone.OutDecision.domain.vote.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    @Query("SELECT DISTINCT vo.options.post.id FROM Vote v JOIN v.voteToOptionsList vo WHERE v.member.id = :memberId")
    List<Long> findPostIdsByMemberId(Long memberId);
}
