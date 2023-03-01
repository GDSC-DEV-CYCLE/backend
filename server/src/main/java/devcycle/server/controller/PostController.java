package devcycle.server.controller;

import devcycle.server.domain.post.Post;
import devcycle.server.domain.user.User;
import devcycle.server.dto.post.CreatePostDto;
import devcycle.server.dto.user.SignupRequestDto;
import devcycle.server.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/post/list")
    public ResponseEntity<Page<Post>> getPostList(@PageableDefault Pageable pageable) throws Exception {
        Page<Post> postList = postService.getPostList(pageable);
        return ResponseEntity.ok().body(postList);
    }

    @PostMapping("/post")
    public ResponseEntity<Post> createPost(@RequestBody CreatePostDto dto) throws Exception {
        Post post = postService.createPost(dto);
        return ResponseEntity.ok().body(post);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<Post> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok().body(postService.getPost(postId));
    }
}