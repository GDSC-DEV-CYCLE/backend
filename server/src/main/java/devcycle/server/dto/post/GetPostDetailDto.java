package devcycle.server.dto.post;

import devcycle.server.domain.post.PostType;

import java.time.LocalDateTime;
import java.util.List;

public class GetPostDetailDto {
    private String title;

    private String content;

    private String author;

    private PostType postType;

    private List<String> hashtags;

    private LocalDateTime createdAt;

}
