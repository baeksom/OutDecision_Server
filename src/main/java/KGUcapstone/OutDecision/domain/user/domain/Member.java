package KGUcapstone.OutDecision.domain.user.domain;

import KGUcapstone.OutDecision.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String email;
    git push 
    private String password;

    @Column(length = 20)
    private String name;

    @Column(length = 10)
    private String socialType;

    @Column(nullable = false, length = 20)
    private String nickname;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer point;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer bumps;

    @Column(length = 30)
    private String phone;

    @Column(length = 20)
    private String userTitle;

    //default 기본이미지
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT '기본 이미지 URL'")
    private String userImg;

    @Column(length = 30)
    private String userRole;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberView> memberViewList = new ArrayList<>();
}
