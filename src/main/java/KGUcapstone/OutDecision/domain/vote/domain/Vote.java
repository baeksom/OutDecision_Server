package KGUcapstone.OutDecision.domain.vote.domain;

import KGUcapstone.OutDecision.domain.options.domain.Options;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vote")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor


public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 투표 아이디

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 유저 아이디

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Options options; // 옵션 아이디
}
