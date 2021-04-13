package ru.edjll.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.group.user.GroupUserDtoForGroupPage;
import ru.edjll.backend.dto.group.user.GroupUserDtoForSubscribe;
import ru.edjll.backend.dto.group.user.GroupUserDtoForSubscribersPage;
import ru.edjll.backend.service.GroupUserService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/group")
@Validated
public class GroupUserController {

    private final GroupUserService groupUserService;

    public GroupUserController(GroupUserService groupUserService) {
        this.groupUserService = groupUserService;
    }

    @GetMapping("/users")
    public Page<GroupUserDtoForGroupPage> getDtoByGroupId(
            @RequestParam
            @NotNull
            @Positive
            @Exists(table = "groups", column = "id") Long groupId,
            @RequestParam
            @NotNull Integer page,
            @RequestParam
            @NotNull
            @Positive Integer pageSize
    ) {
        return groupUserService.getDtoByGroupId(groupId, page, pageSize);
    }

    @GetMapping("/subscribers")
    public Page<GroupUserDtoForSubscribersPage> getDtoByGroupId(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam Long groupId,
            @RequestParam Optional<String> firstName,
            @RequestParam Optional<String> lastName,
            @RequestParam Optional<Long> countryId,
            @RequestParam Optional<Long> cityId
    ) {
        return groupUserService.getSubscribers(page, size, groupId, firstName, lastName, countryId, cityId);
    }

    @PostMapping("/subscribe")
    @ResponseStatus(HttpStatus.CREATED)
    public void subscribe(@RequestBody @Valid GroupUserDtoForSubscribe groupUserDtoForSubscribe, Principal principal) {
        groupUserService.subscribe(groupUserDtoForSubscribe, principal);
    }

    @DeleteMapping("/unsubscribe")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribe(
            @RequestParam
            @NotNull
            @Positive
            @Exists(table = "groups", column = "id") Long groupId,
            Principal principal
    ) {
        groupUserService.unsubscribe(groupId, principal);
    }
}
