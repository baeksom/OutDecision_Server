package KGUcapstone.OutDecision.domain.title.domain;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import io.swagger.models.auth.In;
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
    public void setFashionista_cnt(Integer fashionista_cnt) {
        this.fashionista_cnt = fashionista_cnt;
    }
    public void setFoodie_cnt(Integer foodie_cnt) {
        this.foodie_cnt = foodie_cnt;
    }
    public void setTraveler_cnt(Integer traveler_cnt) {
        this.traveler_cnt = traveler_cnt;
    }
    public void setCeo_cnt(Integer ceo_cnt) {
        this.ceo_cnt = ceo_cnt;
    }
    public void setRomantist_cnt(Integer romantist_cnt) {
        this.romantist_cnt = romantist_cnt;
    }
    public void setHobbyist_cnt(Integer hobbyist_cnt) {
        this.hobbyist_cnt = hobbyist_cnt;
    }
}
