package KGUcapstone.OutDecision.domain.user.domain;

import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.likes.domain.Likes;
import KGUcapstone.OutDecision.domain.post.domain.Post;
import KGUcapstone.OutDecision.domain.title.domain.Missions;
import KGUcapstone.OutDecision.domain.title.domain.Title;
import KGUcapstone.OutDecision.domain.vote.domain.Vote;
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

    @Column(nullable = false, length = 50, unique = true)
    private String email;

    private String password;

    @Column(length = 20)
    private String name;

    @Column(length = 10)
    private String socialType;

    @Column(nullable = false, length = 20, unique = true)
    private String nickname;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer point;

    @ColumnDefault("0")
    @Column(nullable = false)
    private Integer bumps;

    @Column(length = 20)
    private String userTitle;

    @Column(nullable = false)
    private String userImg;

    @Column(length = 30)
    private String userRole;

    public void updateMember(String name, String nickname) {
        this.name = name;
        this.nickname = nickname;
    }

    public void updateUserImg(String userImg) {
        this.userImg = userImg;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberView> memberViewList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Comments> commentsList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Likes> likesList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Vote> voteList = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Missions missions;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private Title title;

    public void updateUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }

    public void updateBumps(int bumps) {
        this.bumps = bumps;
    }
}
