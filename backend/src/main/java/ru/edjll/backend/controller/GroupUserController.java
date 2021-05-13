package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.group.user.GroupUserDtoWrapperForGroupPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;
import ru.edjll.backend.service.GroupUserService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/groups/{groupId}/users")
@Validated
public class GroupUserController {

    private final GroupUserService groupUserService;

    public GroupUserController(GroupUserService groupUserService) {
        this.groupUserService = groupUserService;
    }

    @GetMapping
    public List<UserInfoDtoForSearch> getDtoByGroupId(
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size,
            @PathVariable @Positive @Exists(table = "groups", column = "id") Long groupId,
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "-1") Long countryId,
            @RequestParam(defaultValue = "-1") Long cityId,
            Principal principal
    ) {
        return groupUserService.getSubscribers(Optional.ofNullable(principal), page, size, groupId, firstName, lastName, countryId, cityId);
    }

    @GetMapping("/cards")
    public GroupUserDtoWrapperForGroupPage getDtoByGroupId(
            @PathVariable @Positive @Exists(table = "groups", column = "id") Long groupId,
            @RequestParam @NotNull @Positive Integer size,
            Principal principal
    ) {
        return groupUserService.getUsersWithUserByUserId(groupId, Optional.ofNullable(principal), size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribe(
            @PathVariable @Positive @Exists(table = "groups", column = "id") Long groupId,
            Principal principal
    ) {
        groupUserService.subscribe(groupId, principal);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(
            @PathVariable @Positive @Exists(table = "groups", column = "id") Long groupId,
            Principal principal
    ) {
        groupUserService.unsubscribe(groupId, principal);
    }
}
