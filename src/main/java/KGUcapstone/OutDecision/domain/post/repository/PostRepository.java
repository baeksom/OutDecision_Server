package KGUcapstone.OutDecision.domain.post.repository;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByMemberId(Long memberId, Sort createdAt);

    List<Post> findAllByIdIn(List<Long> ids, Sort sort);

    Page<Post> findAllByMember(Member member, PageRequest pageRequest);

    Page<Post> findAllByMemberAndStatus(Member member, Status status, PageRequest of);

    Page<Post> findAllByIdIn(List<Long> postIds, PageRequest of);

    Page<Post> findAllByIdInAndStatus(List<Long> postIds, Status status, PageRequest of);

}
