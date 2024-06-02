package KGUcapstone.OutDecision.domain.post.repository;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContaining(String keyword);
    List<Post> findByContentContaining(String keyword);
    @Query("SELECT DISTINCT p FROM Post p JOIN p.optionsList o WHERE o.body LIKE %:keyword%")
    List<Post> findByOptionsContaining(String keyword);
    List<Post> findAllByMemberId(Long memberId, Sort bumpsTime);
    List<Post> findAllByIdIn(List<Long> ids, Sort sort);
    Page<Post> findAllByMember(Member member, PageRequest pageRequest);
    Page<Post> findAllByMemberAndStatus(Member member, Status status, PageRequest of);
    Page<Post> findAllByIdIn(List<Long> postIds, PageRequest of);
    Page<Post> findAllByIdInAndStatus(List<Long> postIds, Status status, PageRequest of);
    List<Post> findByHotTrue(Pageable p);
    List<Post> findTop6ByStatusOrderByDeadlineDesc(Status status, Pageable p);
}
