package KGUcapstone.OutDecision.domain.title.repository;

import KGUcapstone.OutDecision.domain.title.domain.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TitleRepository extends JpaRepository<Title, Long> {
    @Query("SELECT " +
            "SUM(CASE WHEN t.ceo = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.fashionista = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.foodie = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.greedy = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.romantist = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.sprout = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.traveler = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.first = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.second = true THEN 1 ELSE 0 END) + " +
            "SUM(CASE WHEN t.third = true THEN 1 ELSE 0 END) " +
            "FROM Title t WHERE t.member.id = :memberId")
    int countTrueColumnsForMember(Long memberId);
}
