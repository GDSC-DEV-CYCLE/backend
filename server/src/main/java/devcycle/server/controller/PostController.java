package devcycle.server.controller;

import devcycle.server.domain.post.Post;
import devcycle.server.domain.user.User;
import devcycle.server.dto.post.CreatePostDto;
import devcycle.server.dto.user.SignupRequestDto;
import devcycle.server.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/create-post")
    public ResponseEntity<Post> createPost(@RequestBody CreatePostDto dto) throws Exception {
        Post post = postService.createPost(dto);
        return ResponseEntity.ok().body(post);
    }
}
