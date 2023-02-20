package devcycle.server.service;

import devcycle.server.config.JwtTokenProvider;
import devcycle.server.domain.Post;
import devcycle.server.domain.PostRepository;
import devcycle.server.domain.like.Likes;
import devcycle.server.domain.like.LikesRepository;
import devcycle.server.domain.user.User;
import devcycle.server.domain.user.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class LikeService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public Boolean likePost(Map<String, String> requestHeader, Long postId) {
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
        Likes likes = Likes.builder().post(post).user(user).build();
        // todo: post likeCount += 1
        likesRepository.save(likes);
        return true;
    }

    public Boolean unlikePost(Map<String, String> requestHeader, Long postId) {
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
        List<Likes> likes = likesRepository.findByUserIdAndPostId(user.getId(), post.getId());
        // todo: post likeCount -= 1
        likesRepository.deleteAll(likes);
        return true;
    }
}
