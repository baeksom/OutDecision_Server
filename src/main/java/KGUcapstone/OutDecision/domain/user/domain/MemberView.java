package KGUcapstone.OutDecision.domain.user.domain;

import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MemberView {
    
    //사용자 조회기록 id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(columnDefinition = "integer default 0")
    private Integer views;
}
