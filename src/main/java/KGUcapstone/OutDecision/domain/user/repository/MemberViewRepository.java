package KGUcapstone.OutDecision.domain.user.repository;

import KGUcapstone.OutDecision.domain.user.domain.MemberView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberViewRepository extends JpaRepository<MemberView, Long> {

    @Query("SELECT mv FROM MemberView mv WHERE mv.member = :memberId")
    List<MemberView> findMemberViewsByMemberId(Long memberId);
}
