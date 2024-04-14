package KGUcapstone.OutDecision.domain.post.repository;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT COUNT(*) FROM Post p " +
            "WHERE p.member = :memberId AND p.category = :category " +
            "AND p.hot = true")
    int getCntHotByMemberIdAndCategory(Long memberId, Category category);
}
