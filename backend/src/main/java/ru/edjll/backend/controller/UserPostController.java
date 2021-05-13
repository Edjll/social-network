package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.user.post.UserPostDtoForSave;
import ru.edjll.backend.dto.user.post.UserPostDtoForUpdate;
import ru.edjll.backend.service.UserPostService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserPostController {

    private final UserPostService userPostService;

    public UserPostController(UserPostService userPostService) {
        this.userPostService = userPostService;
    }

    @GetMapping("/{userId}/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getAllPostDtoByUserId(
            @PathVariable @Exists(table = "user_entity", column = "id", message = "{post.userId.exists}") String userId,
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size
    ) {
        return userPostService.getAllPostDtoByUserId(userId, page, size);
    }

    @PostMapping("/posts")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto save(
            Principal principal,
            @RequestBody @Valid UserPostDtoForSave userPostDtoForSave
    ) {
        return userPostService.save(principal, userPostDtoForSave);
    }

    @PutMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public PostDto update(
            @PathVariable @Positive(message = "{post.id.positive}") Long id,
            Principal principal,
            @RequestBody @Valid UserPostDtoForUpdate userPostDtoForUpdate
    ) {
        return userPostService.update(id, principal, userPostDtoForUpdate);
    }

    @DeleteMapping("/posts/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable @Positive Long id,
            JwtAuthenticationToken principal
    ) {
        userPostService.delete(id, principal);
    }
}
