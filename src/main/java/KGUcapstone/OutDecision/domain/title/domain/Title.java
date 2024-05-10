package KGUcapstone.OutDecision.domain.title.domain;

import KGUcapstone.OutDecision.domain.user.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean sprout;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean fashionista;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean foodie;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean traveler;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean ceo;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean romantist;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean hobbyist;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean greedy;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean first;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean second;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean third;

    public void setFirst(Boolean first) {
        this.first = first;
    }

    public void setSecond(Boolean second) {
        this.second = second;
    }

    public void setThird(Boolean third) {
        this.third = third;
    }

    public void setFashionista(Boolean fashionista) {
        this.fashionista = fashionista;
    }

    public void setFoodie(Boolean foodie) {
        this.foodie = foodie;
    }

    public void setTraveler(Boolean traveler) {
        this.traveler = traveler;
    }

    public void setCeo(Boolean ceo) {
        this.ceo = ceo;
    }

    public void setRomantist(Boolean romantist) {
        this.romantist = romantist;
    }

    public void setHobbyist(Boolean hobbyist) {
        this.hobbyist = hobbyist;
    }

    public void setGreedy(Boolean greedy) {
        this.greedy = greedy;
    }
}
