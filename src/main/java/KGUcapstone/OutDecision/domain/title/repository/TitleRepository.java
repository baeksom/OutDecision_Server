package KGUcapstone.OutDecision.domain.title.repository;

import KGUcapstone.OutDecision.domain.title.domain.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TitleRepository extends JpaRepository<Title, Long> {
    List<Title> findByFirstTrue();

    List<Title> findBySecondTrue();

    List<Title> findByThirdTrue();

    Title findByMemberId(long memberId);
}
