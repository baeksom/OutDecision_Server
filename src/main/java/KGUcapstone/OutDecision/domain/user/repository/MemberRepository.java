package KGUcapstone.OutDecision.domain.user.repository;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT nickname FROM Member WHERE id = :memberId")
    String findNicknameById(Long memberId);
}
=======

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);
    Member findByNickname(String nickname);
}
>>>>>>> develop
