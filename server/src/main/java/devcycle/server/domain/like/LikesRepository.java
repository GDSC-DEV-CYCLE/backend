package devcycle.server.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByUserIdAndPostId(Long userId, Long postId);
}
