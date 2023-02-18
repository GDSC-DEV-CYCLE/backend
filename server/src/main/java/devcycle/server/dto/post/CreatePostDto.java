package devcycle.server.dto.post;

import devcycle.server.domain.post.Post;
import devcycle.server.domain.post.PostType;
import devcycle.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreatePostDto {
    private String title;

    private String content;

    private PostType postType;

    @Builder
    public CreatePostDto(String title, String content, PostType postType) {
        this.title = title;
        this.content = content;
        this.postType = postType;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .postType(postType)
                .build();
    }
}
