package KGUcapstone.OutDecision.domain.comment.domain;


import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.user.domain.User;
import KGUcapstone.OutDecision.global.common.BaseEntity;
import jakarta.persistence.*;

import lombok.*;


@Entity
@Table(name = "comments")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor


public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 댓글아이디

    @Column(name = "comment_body", columnDefinition = "TEXT", nullable = false)
    private String body; // 내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 유저 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 게시글 아이디


}
