package devcycle.server.controller;

import devcycle.server.dto.comment.CreateCommentDto;
import devcycle.server.dto.comment.GetCommentDto;
import devcycle.server.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/comment/post/{postId}")
    public ResponseEntity<Boolean> createComment(@RequestHeader Map<String, String> requestHeader, @PathVariable Long postId, @RequestBody CreateCommentDto dto) {
        commentService.createComment(requestHeader, postId, dto.getContent());
        return ResponseEntity.ok().body(true);
    }

    @GetMapping("/comments/post/{postId}")
    public ResponseEntity<List<GetCommentDto>> getCommentList(@PathVariable Long postId) {
        return ResponseEntity.ok().body(commentService.getCommentList(postId));
    }

    @
}
