package ru.edjll.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.post.PostDtoForDelete;
import ru.edjll.backend.dto.post.PostDtoForSave;
import ru.edjll.backend.dto.post.PostDtoForUpdate;
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

    public PostDto save(PostDtoForSave postDtoForSave) {
        Post savedPost = postRepository.save(postDtoForSave.toPost());
        return new PostDto(savedPost);
    }

    public PostDto update(PostDtoForUpdate postDtoForUpdate) {
        Post savedPost = postRepository.save(postDtoForUpdate.toPost());
        return new PostDto(savedPost);
    }

    public void delete(PostDtoForDelete postDtoForDelete) {
        postRepository.deleteById(postDtoForDelete.getId());
    }

    public Collection<PostDto> getAllPostDtoByUserId(String id) {
        return postRepository.getAllPostDtoByUserId(id);
    }
}
