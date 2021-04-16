package ru.edjll.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.group.user.GroupUserDtoForGroupPage;
import ru.edjll.backend.dto.group.user.GroupUserDtoForSubscribe;
import ru.edjll.backend.dto.group.user.GroupUserDtoForSubscribersPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;
import ru.edjll.backend.service.GroupUserService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.security.Principal;
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
    public Page<UserInfoDtoForSearch> getDtoByGroupId(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @PathVariable Long groupId,
            @RequestParam Optional<String> firstName,
            @RequestParam Optional<String> lastName,
            @RequestParam Optional<Long> countryId,
            @RequestParam Optional<Long> cityId,
            Principal principal
    ) {
        return groupUserService.getSubscribers(Optional.ofNullable(principal), page, size, groupId, firstName, lastName, countryId, cityId);
    }

    @GetMapping("/{userId}")
    public Page<GroupUserDtoForGroupPage> getDtoByGroupId(
            @PathVariable Long groupId,
            @PathVariable String userId,
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        return groupUserService.getUsersWithUserByUserId(groupId, userId, page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribe(
            @PathVariable
            @NotNull
            @Positive
            @Exists(table = "groups", column = "id") Long groupId,
            Principal principal) {
        groupUserService.subscribe(groupId, principal);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(
            @PathVariable
            @NotNull
            @Positive
            @Exists(table = "groups", column = "id") Long groupId,
            Principal principal
    ) {
        groupUserService.unsubscribe(groupId, principal);
    }
}
