package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.post.PostDtoForDelete;
import ru.edjll.backend.dto.post.PostDtoForSave;
import ru.edjll.backend.dto.post.PostDtoForUpdate;
import ru.edjll.backend.service.PostService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@RestController
@RequestMapping("/post")
@Validated
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<PostDto> getAllPostDtoByUserId(
            @RequestParam
            @NotEmpty(message = "{post.userId.notEmpty}")
            @Exists(table = "user_entity", column = "id", message = "{post.userId.exists}") String userId
    ) {
        return postService.getAllPostDtoByUserId(userId);
    }

    @PreAuthorize("principal.getClaim('sub') == #postDtoForSave.userId")
    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public PostDto save(@RequestBody @Valid PostDtoForSave postDtoForSave) {
        return postService.save(postDtoForSave);
    }

    @PreAuthorize("principal.getClaim('sub') == #postDtoForUpdate.userId")
    @PutMapping("/update")
    public PostDto update(@RequestBody @Valid PostDtoForUpdate postDtoForUpdate) {
        return postService.update(postDtoForUpdate);
    }

    @PreAuthorize("principal.getClaim('sub') == #postDtoForDelete.userId or hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public void delete(@RequestBody @Valid PostDtoForDelete postDtoForDelete) {
        postService.delete(postDtoForDelete);
    }
}
