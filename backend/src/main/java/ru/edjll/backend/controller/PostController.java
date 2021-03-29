package ru.edjll.backend.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.PostDto;
import ru.edjll.backend.entity.Post;
import ru.edjll.backend.service.PostService;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<PostDto> getAllPostDtoByUserId(@RequestParam String userId) {
        return postService.getAllPostDtoByUserId(userId);
    }

    @PostMapping("/save")
    public PostDto save(
            @RequestBody Post post,
            @AuthenticationPrincipal Principal principal
    ) {
        return postService.save(post, principal);
    }

    @PutMapping("/edit")
    public PostDto edit(
            @RequestBody Post post,
            @AuthenticationPrincipal Principal principal
    ) {
        return postService.edit(post, principal);
    }

    @DeleteMapping("/delete")
    public void delete(
            @RequestBody Post post,
            @AuthenticationPrincipal Principal principal
    ) {
        postService.delete(post, principal);
    }
}
