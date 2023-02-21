package devcycle.server.dto.post;

import devcycle.server.domain.post.Post;
import devcycle.server.domain.post.PostType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreatePostDto {
    private String title;

    private String content;

    private PostType postType;

    private String hashtags;

    @Builder
    public CreatePostDto(String title, String content, PostType postType, List<String> hashtags) {
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.hashtags = hashtags.toString();
    }

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .postType(postType)
                .hashtags(hashtags)
                .build();
    }
}
