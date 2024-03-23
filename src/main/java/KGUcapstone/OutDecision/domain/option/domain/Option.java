package KGUcapstone.OutDecision.domain.option.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "options")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Option {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 옵션 아이디

    @Column(name = "option_body", length = 30)
    private String body; // 옵션 내용

    @Column(name = "photo_url")
    private String url; // 사진 url

    /*@ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;*/
}