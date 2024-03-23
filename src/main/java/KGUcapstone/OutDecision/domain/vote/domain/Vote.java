package KGUcapstone.OutDecision.domain.vote.domain;

import KGUcapstone.OutDecision.domain.option.domain.Option;
import KGUcapstone.OutDecision.domain.user.domain.User;
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
    @JoinColumn(name = "user_id")
    private User user; // 유저 아이디

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id")
    private Option option; // 옵션 아이디
}
