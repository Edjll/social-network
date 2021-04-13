package ru.edjll.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.group.post.GroupPostDtoForGroupPage;
import ru.edjll.backend.dto.group.post.GroupPostDtoForSave;
import ru.edjll.backend.dto.group.post.GroupPostDtoForUpdate;
import ru.edjll.backend.service.GroupPostService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/group/post")
@Validated
public class GroupPostController {

    private final GroupPostService groupPostService;

    public GroupPostController(GroupPostService groupPostService) {
        this.groupPostService = groupPostService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<GroupPostDtoForGroupPage> getByGroupId(
            @RequestParam
            @NotNull
            @Positive
            @Exists(table = "groups", column = "id") Long groupId,
            @RequestParam
            @NotNull
            @PositiveOrZero Integer page,
            @RequestParam
            @NotNull
            @Positive Integer pageSize
    ) {
        return groupPostService.getDtoByGroupId(groupId, page, pageSize);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<GroupPostDtoForGroupPage> save(@RequestBody @Valid GroupPostDtoForSave groupPostDtoForSave, Principal principal) {
        return groupPostService.save(groupPostDtoForSave, principal);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public Optional<GroupPostDtoForGroupPage> update(@RequestBody @Valid GroupPostDtoForUpdate groupPostDtoForUpdate, Principal principal) {
        return groupPostService.update(groupPostDtoForUpdate, principal);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @RequestParam
            @NotNull
            @Positive
            @Exists(table = "group_post", column = "id") Long id,
            JwtAuthenticationToken principal
    ) {
        groupPostService.delete(id, principal);
    }
}
