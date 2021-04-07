package ru.edjll.backend.service;

import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.post.PostDtoForDelete;
import ru.edjll.backend.dto.post.PostDtoForSave;
import ru.edjll.backend.dto.post.PostDtoForUpdate;
import ru.edjll.backend.entity.Post;
import ru.edjll.backend.repository.PostRepository;

import java.util.Collection;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostDto save(PostDtoForSave postDtoForSave) {
        Post savedPost = postRepository.save(postDtoForSave.toPost());
        return this.getPostDtoById(savedPost.getId());
    }

    public PostDto update(PostDtoForUpdate postDtoForUpdate) {
        Post savedPost = postRepository.save(postDtoForUpdate.toPost());
        return this.getPostDtoById(savedPost.getId());
    }

    public void delete(PostDtoForDelete postDtoForDelete) {
        postRepository.deleteById(postDtoForDelete.getId());
    }

    public Collection<PostDto> getAllPostDtoByUserId(String id) {
        return postRepository.getAllPostDtoByUserId(id);
    }

    public PostDto getPostDtoById(long id) {
        return postRepository.getPostDtoById(id);
    }
}
