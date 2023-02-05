package devcycle.server.dto;

import devcycle.server.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreatePostDto {
    private String title;

    @Builder
    public CreatePostDto(String title) {
        this.title = title;
    }

    public Post toEntity() {
        return Post.builder().title(title).build();
    }
}
