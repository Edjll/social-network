package ru.edjll.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.user.friend.UserFriendDtoForSave;
import ru.edjll.backend.dto.user.friend.UserFriendDtoForUpdate;
import ru.edjll.backend.dto.user.info.UserInfoDtoForFriendsPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSubscribersPage;
import ru.edjll.backend.service.UserFriendService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}")
public class UserFriendController {

    private final UserFriendService userFriendService;

    public UserFriendController(UserFriendService userFriendService) {
        this.userFriendService = userFriendService;
    }

    @GetMapping("/friends")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserInfoDtoForFriendsPage> getUsers(
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size,
            @PathVariable @Exists(table = "user_entity", column = "id") String userId,
            @RequestParam Optional<String> firstName,
            @RequestParam Optional<String> lastName,
            @RequestParam Optional<Long> countryId,
            @RequestParam Optional<Long> cityId
    ) {
        return userFriendService.getFriends(page, size, userId, firstName, lastName, countryId, cityId);
    }

    @GetMapping("/subscribers")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserInfoDtoForSubscribersPage> getSubscribersForUserPage(
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size,
            @PathVariable @Exists(table = "user_entity", column = "id") String userId,
            @RequestParam Optional<String> firstName,
            @RequestParam Optional<String> lastName,
            @RequestParam Optional<Long> countryId,
            @RequestParam Optional<Long> cityId
    ) {
        return userFriendService.getSubscribers(page, size, userId, firstName, lastName, countryId, cityId);
    }

    @PostMapping("/friends")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(
            @PathVariable String userId,
            Principal principal
    ) {
        this.userFriendService.save(userId, principal);
    }

    @PutMapping("/friends")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable String userId,
            Principal principal
    ) {
        this.userFriendService.update(userId, principal);
    }

    @DeleteMapping("/friends")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable String userId,
            Principal principal
    ) {
        this.userFriendService.delete(userId, principal);
    }
}
