package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.user.post.UserPostDtoForSave;
import ru.edjll.backend.dto.user.post.UserPostDtoForUpdate;
import ru.edjll.backend.service.UserPostService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/users")
@Validated
public class PostController {

    private final UserPostService userPostService;

    public PostController(UserPostService userPostService) {
        this.userPostService = userPostService;
    }

    @GetMapping("/{userId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public Collection<PostDto> getAllPostDtoByUserId(
            @PathVariable
            @NotEmpty(message = "{post.userId.notEmpty}")
            @Exists(table = "user_entity", column = "id", message = "{post.userId.exists}") String userId
    ) {
        return userPostService.getAllPostDtoByUserId(userId);
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto save(
            Principal principal,
            @RequestBody
            @Valid UserPostDtoForSave userPostDtoForSave
    ) {
        return userPostService.save(principal, userPostDtoForSave);
    }

    @PutMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PostDto update(
            @PathVariable
            @NotNull(message = "{post.id.notNull}")
            @Positive(message = "{post.id.positive}")
            @Exists(table = "post", column = "id", message = "{post.id.exists}") Long id,
            Principal principal,
            @RequestBody
            @Valid UserPostDtoForUpdate userPostDtoForUpdate
    ) {
        return userPostService.update(id, principal, userPostDtoForUpdate);
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable
            @NotNull(message = "{post.id.notNull}")
            @Positive(message = "{post.id.positive}")
            @Exists(table = "post", column = "id", message = "{post.id.exists}") Long id,
            Principal principal
    ) {
        userPostService.delete(id, principal);
    }
}
