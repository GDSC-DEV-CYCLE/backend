package devcycle.server.dto.post;

import devcycle.server.domain.post.Post;
import devcycle.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CreatePostDto {
    private String title;

    private String content;

    @Builder
    public CreatePostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}
