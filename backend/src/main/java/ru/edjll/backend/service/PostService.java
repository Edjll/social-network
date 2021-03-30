package ru.edjll.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.PostDto;
import ru.edjll.backend.entity.Post;
import ru.edjll.backend.repository.PostRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostDto save(Post post, Principal principal) {
        if (post.getUser() == null || !principal.getName().equals(post.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Don't have rights");
        }
        post.setCreatedDate(LocalDateTime.now());
        Post savedPost = postRepository.save(post);
        return postRepository.getPostDtoById(savedPost.getId());
    }

    public PostDto update(Post post, Principal principal) {
        if (post.getUser() == null || !principal.getName().equals(post.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Don't have rights");
        }
        post.setModifiedDate(LocalDateTime.now());
        Post savedPost = postRepository.save(post);
        return postRepository.getPostDtoById(savedPost.getId());
    }

    public void delete(Post post, Principal principal) {
        if (post.getUser() == null || !principal.getName().equals(post.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Don't have rights");
        }
        postRepository.deleteById(post.getId());
    }

    public Collection<PostDto> getAllPostDtoByUserId(String id) {
        return postRepository.getAllPostDtoByUserId(id);
    }
}
