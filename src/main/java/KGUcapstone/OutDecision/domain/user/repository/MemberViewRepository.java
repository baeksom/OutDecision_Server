package KGUcapstone.OutDecision.domain.user.repository;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.user.domain.MemberView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberViewRepository extends JpaRepository<MemberView, Long> {
    MemberView findByMemberIdAndCategory(Long memberId, Category category);
    List<MemberView> findByMemberId(Long memberId);
}
