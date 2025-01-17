package KGUcapstone.OutDecision.domain.post.domain;

import KGUcapstone.OutDecision.domain.comments.domain.Comments;
import KGUcapstone.OutDecision.domain.likes.domain.Likes;
import KGUcapstone.OutDecision.domain.notifications.domain.Notifications;
import KGUcapstone.OutDecision.domain.options.domain.Options;
import KGUcapstone.OutDecision.domain.post.domain.enums.Category;
import KGUcapstone.OutDecision.domain.post.domain.enums.Gender;
import KGUcapstone.OutDecision.domain.post.domain.enums.Status;
import KGUcapstone.OutDecision.domain.user.domain.Member;
import KGUcapstone.OutDecision.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(columnDefinition = "integer default 0")
    private Integer views;

    @Column(columnDefinition = "integer default 0")
    private Integer likes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false, columnDefinition = "DATETIME")
    private Date deadline;

    @ColumnDefault("false")
    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean pluralVoting;

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean hot;

    @Column(nullable = false, updatable = true, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime bumpsTime;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('progress', 'end') DEFAULT 'progress'")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('female', 'male', 'all') DEFAULT 'all'")
    private Gender gender;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Likes> likesList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comments> commentsList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Options> optionsList = new ArrayList<>();

    public void setStatus(Status status) {
        this.status = status;
    }
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Notifications> notificationsList = new ArrayList<>();

    public void incrementViews() {
        views++;
    }

    public void setOptionsList(List<Options> optionsList) {
    }

    public void updatePost(String title, String content, Category category,
                           Date deadline, Boolean pluralVoting, Gender gender) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.deadline = deadline;
        this.pluralVoting = pluralVoting;
        this.gender = gender;
    }

    public void updateLikes(Integer likes) {
        this.likes = likes;
    }

    public void updateHot(boolean hot) {
        this.hot = hot;
    }

    public void updateBumpsTime() {
        this.bumpsTime = LocalDateTime.now();

    }
}
