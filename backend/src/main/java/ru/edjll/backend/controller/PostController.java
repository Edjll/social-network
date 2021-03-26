package ru.edjll.backend.controller;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Collection<PostDto>> getAllPostDtoByUserId(
        @RequestParam String userId
    ) {
        return ResponseEntity.ok().body(postService.getAllPostDtoByUserId(userId));
    }

    @PostMapping("/save")
    public ResponseEntity<PostDto> save(@RequestBody Post post, @AuthenticationPrincipal Principal principal) {
        if (post.getUser() == null || !principal.getName().equals(post.getUser().getId())) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(postService.save(post));
    }

    @PutMapping("/edit")
    public ResponseEntity<PostDto> edit(@RequestBody Post post, @AuthenticationPrincipal Principal principal) {
        if (post.getUser() == null || !principal.getName().equals(post.getUser().getId())) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(postService.edit(post));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody Post post, @AuthenticationPrincipal Principal principal) {
        if (post.getUser() == null || !principal.getName().equals(post.getUser().getId())) {
            return ResponseEntity.badRequest().body(null);
        }
        postService.delete(post);
        return ResponseEntity.ok().body(null);
    }
}
