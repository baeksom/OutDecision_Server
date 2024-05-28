package KGUcapstone.OutDecision.domain.user.repository;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.user.domain.MemberView;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MemberViewRepository extends JpaRepository<MemberView, Long> {
    MemberView findByMemberIdAndCategory(Long memberId, Category category);
}
