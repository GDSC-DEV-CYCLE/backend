package devcycle.server.controller;

import devcycle.server.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LikesController {
    private final LikeService likeService;

    @PostMapping("like/post/{postId}")
    public ResponseEntity<Boolean> likePost(@RequestHeader Map<String, String> requestHeader, @PathVariable Long postId) {
        return ResponseEntity.ok().body(likeService.likePost(requestHeader, postId));
    }

    @PostMapping("unlike/post/{postId}")
    public ResponseEntity<Boolean> unlikePost(@RequestHeader Map<String, String> requestHeader, @PathVariable Long postId) {
        return ResponseEntity.ok().body(likeService.unlikePost(requestHeader, postId));
    }
}
