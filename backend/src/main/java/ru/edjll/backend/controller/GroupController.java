package ru.edjll.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.group.*;
import ru.edjll.backend.service.GroupService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/page")
    public Page<GroupDtoForSearch> getAll(
            @RequestParam Integer page,
            @RequestParam Integer pageSize
    ) {
        return groupService.getAll(page, pageSize);
    }

    @GetMapping("/{address}")
    public Optional<GroupDto> getByAddress(
            @PathVariable
            @NotEmpty
            @Exists(table = "groups", column = "address") String address
    ) {
        return groupService.getDtoByAddress(address);
    }

    @GetMapping
    public Page<GroupDtoForUserPage> getByUserId(
            @RequestParam
            @NotEmpty
            @Exists(table = "user_entity", column = "id") String userId,
            @RequestParam
            @NotNull
            @PositiveOrZero Integer page,
            @RequestParam
            @NotNull
            @Positive Integer pageSize
    ) {
        return groupService.getDtoByUserId(userId, page, pageSize);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid GroupDtoForSave groupDtoForSave, Principal principal) {
        groupService.save(groupDtoForSave, principal);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid GroupDtoForUpdate groupDtoForUpdate, Principal principal) {
        groupService.update(groupDtoForUpdate, principal);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam @NotNull @Positive @Exists(table = "groups", column = "id") Long id, JwtAuthenticationToken principal) {
        groupService.delete(id, principal);
    }
}
