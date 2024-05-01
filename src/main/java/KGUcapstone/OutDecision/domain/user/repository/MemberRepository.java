package KGUcapstone.OutDecision.domain.user.repository;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT nickname FROM Member WHERE id = :memberId")
    String findNicknameById(Long memberId);
    @Query("SELECT userImg FROM Member WHERE id = :memberId")
    String findUserImgById(Long memberId);
    Member findByEmail(String email);
    Member findByNickname(String nickname);
}
