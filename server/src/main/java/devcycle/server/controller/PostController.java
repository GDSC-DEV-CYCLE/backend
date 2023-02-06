package devcycle.server.controller;

import devcycle.server.dto.post.CreatePostDto;
import devcycle.server.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/create-post")
    public Long createPost(@RequestBody CreatePostDto dto) {
        return postService.createPost(dto);
    }
}
