package KGUcapstone.OutDecision.domain.title.domain;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Missions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer fashionista_cnt;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer foodie_cnt;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer traveler_cnt;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer ceo_cnt;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer romantist_cnt;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer hobbyist_cnt;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer greedy_cnt;

    public void setGreedy_cnt(Integer greedy_cnt) {
        this.greedy_cnt = greedy_cnt;
    }
}
