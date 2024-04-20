package KGUcapstone.OutDecision.domain.post.repository;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String keyword);
    List<Post> findByContentContaining(String keyword);
    List<Post> findByTitleContainingOrContentContaining(String title, String content);

    List<Post> findByOrderByCreatedAtDesc();
    List<Post> findByOrderByViewsDesc();
    List<Post> findByOrderByLikesDesc();

}
