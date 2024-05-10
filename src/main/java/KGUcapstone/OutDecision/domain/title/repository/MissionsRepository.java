package KGUcapstone.OutDecision.domain.title.repository;

import KGUcapstone.OutDecision.domain.title.domain.Missions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissionsRepository extends JpaRepository<Missions, Long> {
    Missions findAllByMemberId(Long memberId);

    Missions findByMemberId(Long id);
}
