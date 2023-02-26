package devcycle.server.domain.post;

import devcycle.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @Column
    private String content;

    @CreatedDate
    @Column(updatable = false, nullable = true)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @Column
    private PostType postType;

    @Column
    private String hashtags;

    @Column(columnDefinition = "integer default 0")
    private Integer view = 0;


    @Builder
    public Post(String title, String content, PostType postType, String hashtags) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.hashtags = hashtags;
    }
}
