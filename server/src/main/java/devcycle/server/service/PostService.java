package devcycle.server.service;

import devcycle.server.domain.PostRepository;
import devcycle.server.dto.CreatePostDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Long createPost(CreatePostDto dto) {
        return postRepository.save(dto.toEntity()).getId();
    }
}
