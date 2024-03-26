package KGUcapstone.OutDecision.domain.user.repository;

import KGUcapstone.OutDecision.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
