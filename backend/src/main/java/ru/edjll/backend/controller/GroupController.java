package ru.edjll.backend.controller;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.group.GroupDto;
import ru.edjll.backend.dto.group.GroupDtoForSave;
import ru.edjll.backend.dto.group.GroupDtoForSearch;
import ru.edjll.backend.dto.group.GroupDtoForUpdate;
import ru.edjll.backend.service.GroupService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups")
@Validated
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<GroupDtoForSearch> getAll(
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size,
            Principal principal
    ) {
        return groupService.getAll(page, size, Optional.ofNullable(principal));
    }

    @GetMapping("/{address}")
    public GroupDto getByAddress(
            @PathVariable @Length(min = 3, max = 15) String address
    ) {
        return groupService.getDtoByAddress(address);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(
            @RequestBody @Valid GroupDtoForSave groupDtoForSave,
            Principal principal
    ) {
        groupService.save(groupDtoForSave, principal);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable @Positive Long id,
            @RequestBody @Valid GroupDtoForUpdate groupDtoForUpdate,
            Principal principal
    ) {
        groupService.update(id, groupDtoForUpdate, principal);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable @Positive Long id,
            JwtAuthenticationToken principal
    ) {
        groupService.delete(id, principal);
    }
}
