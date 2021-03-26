package ru.edjll.backend.service;

import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.PostDto;
import ru.edjll.backend.entity.Post;
import ru.edjll.backend.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostDto save(Post post) {
        post.setCreatedDate(LocalDateTime.now());
        Post savedPost = postRepository.save(post);
        return postRepository.getPostDtoById(savedPost.getId());
    }

    public PostDto edit(Post post) {
        post.setModifiedDate(LocalDateTime.now());
        Post savedPost = postRepository.save(post);
        return postRepository.getPostDtoById(savedPost.getId());
    }

    public void delete(Post post) {
        postRepository.deleteById(post.getId());
    }

    public Collection<PostDto> getAllPostDtoByUserId(String id) {
        return postRepository.getAllPostDtoByUserId(id);
    }
}
