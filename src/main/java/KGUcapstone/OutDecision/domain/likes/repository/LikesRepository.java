package KGUcapstone.OutDecision.domain.likes.repository;

import KGUcapstone.OutDecision.domain.likes.domain.Likes;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Query("SELECT l.post.id FROM Likes l WHERE l.member.id = :memberId ORDER BY l.createdAt DESC")
    List<Long> findPostIdsByMemberIdOrderByCreatedAtDesc(Long memberId);

    Optional<Likes> findByPostAndMember(Post post, Member member);
}
