package devcycle.server.service;

import devcycle.server.domain.post.Post;
import devcycle.server.domain.post.PostRepository;
import devcycle.server.dto.post.CreatePostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(CreatePostDto dto) throws Exception {
        Post post = dto.toEntity();
        postRepository.save(post);
        return post;
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<Post> getPostList() throws Exception {
        return postRepository.findAll();
    }
}
