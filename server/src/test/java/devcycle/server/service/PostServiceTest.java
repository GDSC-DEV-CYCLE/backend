package devcycle.server.service;

import devcycle.server.domain.post.PostRepository;
import devcycle.server.dto.CreatePostDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Test
    public void 게시글_생성하기() {
        // given
        CreatePostDto dto = CreatePostDto.builder().title("title").build();

        // when
        Long id = postService.createPost(dto);

        // then
        Assertions.assertThat(id).isEqualTo(1);
    }

}
