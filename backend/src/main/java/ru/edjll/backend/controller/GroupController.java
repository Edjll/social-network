package ru.edjll.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public Page<GroupDtoForSearch> getAll(
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            Principal principal
    ) {
        return groupService.getAll(page, pageSize, Optional.ofNullable(principal));
    }

    @GetMapping("/{address}")
    public Optional<GroupDto> getByAddress(
            @PathVariable
            @NotEmpty
            @Exists(table = "groups", column = "address") String address
    ) {
        return groupService.getDtoByAddress(address);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid GroupDtoForSave groupDtoForSave, Principal principal) {
        groupService.save(groupDtoForSave, principal);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable
            @NotNull
            @Positive
            @Exists(table = "groups", column = "id") Long id,
            @RequestBody
            @Valid GroupDtoForUpdate groupDtoForUpdate,
            Principal principal) {
        groupService.update(id, groupDtoForUpdate, principal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable
            @NotNull
            @Positive
            @Exists(table = "groups", column = "id") Long id,
            JwtAuthenticationToken principal) {
        groupService.delete(id, principal);
    }
}
