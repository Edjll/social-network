package ru.edjll.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.user.post.UserPostDtoForSave;
import ru.edjll.backend.dto.user.post.UserPostDtoForUpdate;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.entity.UserPost;
import ru.edjll.backend.repository.UserPostRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;

@Service
public class UserPostService {

    private final UserPostRepository userPostRepository;

    public UserPostService(UserPostRepository userPostRepository) {
        this.userPostRepository = userPostRepository;
    }

    public PostDto save(Principal principal, UserPostDtoForSave userPostDtoForSave) {
        User user = new User();
        user.setId(principal.getName());

        UserPost userPost = userPostDtoForSave.toPost();
        userPost.setUser(user);

        UserPost savedUserPost = userPostRepository.save(userPost);

        return this.getPostDtoById(savedUserPost.getId());
    }

    public PostDto update(Long id, Principal principal, UserPostDtoForUpdate userPostDtoForUpdate) {
        UserPost userPost = userPostRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!userPost.getUser().getId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        userPost.setText(userPostDtoForUpdate.getText());
        userPost.setModifiedDate(LocalDateTime.now());

        UserPost savedUserPost = userPostRepository.save(userPost);
        return this.getPostDtoById(savedUserPost.getId());
    }

    public void delete(Long id, Principal principal) {
        UserPost userPost = userPostRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!userPost.getUser().getId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        userPostRepository.deleteById(id);
    }

    public Page<PostDto> getAllPostDtoByUserId(String id, Integer page, Integer size) {
        return userPostRepository.getAllPostDtoByUserId(id, PageRequest.of(page, size, Sort.by("createdDate").descending()));
    }

    public PostDto getPostDtoById(long id) {
        return userPostRepository.getPostDtoById(id);
    }
}
