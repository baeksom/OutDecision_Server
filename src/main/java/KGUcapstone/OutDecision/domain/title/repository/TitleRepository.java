package KGUcapstone.OutDecision.domain.title.repository;

import KGUcapstone.OutDecision.domain.title.domain.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TitleRepository extends JpaRepository<Title, Long> {
    List<Title> findByFirstTrue();
    List<Title> findBySecondTrue();
    List<Title> findByThirdTrue();
    Title findByMemberId(long memberId);

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

    @Query("SELECT " +
            "GROUP_CONCAT(" +
            "   CASE WHEN t.ceo = true THEN ',💸사장' ELSE '' END, " +
            "   CASE WHEN t.fashionista = true THEN ',🕶️패셔니스타' ELSE '' END, " +
            "   CASE WHEN t.foodie = true THEN ',🍴미식가' ELSE '' END, " +
            "   CASE WHEN t.greedy = true THEN ',😏욕심쟁이' ELSE '' END, " +
            "   CASE WHEN t.romantist = true THEN ',💖로맨티스트' ELSE '' END, " +
            "   CASE WHEN t.sprout = true THEN ',🌱새싹' ELSE '' END, " +
            "   CASE WHEN t.traveler = true THEN ',✈️트래블러' ELSE '' END, " +
            "   CASE WHEN t.first = true THEN ',🥇1위' ELSE '' END, " +
            "   CASE WHEN t.second = true THEN ',🥈2위' ELSE '' END, " +
            "   CASE WHEN t.third = true THEN ',🥉3위' ELSE '' END " +
            ") AS true_titles " +
            "FROM Title t " +
            "WHERE t.member.id = :memberId " +
            "GROUP BY t.member.id")
    String findTrueColumByMemberId(Long memberId);
}
