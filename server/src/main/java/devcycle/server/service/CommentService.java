package devcycle.server.service;

import devcycle.server.config.JwtTokenProvider;
import devcycle.server.domain.Post;
import devcycle.server.domain.PostRepository;
import devcycle.server.domain.comment.Comment;
import devcycle.server.domain.comment.CommentRepository;
import devcycle.server.domain.like.LikesRepository;
import devcycle.server.domain.user.User;
import devcycle.server.domain.user.UserRepository;
import devcycle.server.dto.comment.GetCommentDto;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public Long createComment(Map<String, String> requestHeader, Long postId, String content) {
        String accessToken = requestHeader.get("authorization").substring(7);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new JwtException("유효한 토큰이 아닙니다.");
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        User user;
        Post post;
        try {
            user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }
        try {
            post = postRepository.findById(postId).orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("존재하지 않는 게시글입니다.");
        }
        Comment comment = Comment.builder().user(user).post(post).content(content).build();
        return commentRepository.save(comment).getId();
    }

    public List<GetCommentDto> getCommentList(Long postId) {
        List<Comment> commentList = commentRepository.findByPostId(postId);
        List<GetCommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentDtoList.add(GetCommentDto.builder().id(comment.getId()).userName(comment.getUser().getName()).content(comment.getContent()).build());
        }
        return commentDtoList;
    }

    public void deleteComment(@RequestHeader Map<String, String> requestHeader, Long commentId) {
        String accessToken = requestHeader.get("authorization").substring(7);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new JwtException("유효한 토큰이 아닙니다.");
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
        User user;
        try {
            user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        } catch (Exception e) {
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (!comment.getUser().equals(user)) {
            throw new RuntimeException("댓글 작성자가 아닙니다.");
        }
        commentRepository.delete(comment);
    }
}
