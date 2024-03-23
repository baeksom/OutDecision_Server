package KGUcapstone.OutDecision.domain.options.domain;

import KGUcapstone.OutDecision.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor


public class Options {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 옵션 아이디

    @Column(name = "option_body", nullable = false, length = 30)
    private String body; // 옵션 내용

    @Column(name = "photo_url", columnDefinition = "TEXT")
    private String photoUrl; // 사진 url

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 게시글 아이디
}