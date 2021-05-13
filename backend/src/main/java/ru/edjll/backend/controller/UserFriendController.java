package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.user.friend.UserFriendStatusDto;
import ru.edjll.backend.dto.user.info.UserInfoDtoForFriendsPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSubscribersPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoWrapperForUserCart;
import ru.edjll.backend.service.UserFriendService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}")
public class UserFriendController {

    private final UserFriendService userFriendService;

    public UserFriendController(UserFriendService userFriendService) {
        this.userFriendService = userFriendService;
    }

    @GetMapping("/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<UserInfoDtoForFriendsPage> getFriends(
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size,
            @PathVariable @Exists(table = "user_entity", column = "id") String userId,
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "-1") Long countryId,
            @RequestParam(defaultValue = "-1") Long cityId
    ) {
        return userFriendService.getFriends(page, size, userId, firstName, lastName, countryId, cityId);
    }

    @GetMapping("/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public UserFriendStatusDto friendshipExists(
            @PathVariable @Exists(table = "user_entity", column = "id") String userId,
            @PathVariable @Exists(table = "user_entity", column = "id") String friendId
    ) {
        return userFriendService.friendshipExists(userId, friendId);
    }

    @GetMapping("/friends/cards")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDtoWrapperForUserCart getFriendCards(
            @RequestParam @NotNull @Positive Integer size,
            @PathVariable @Exists(table = "user_entity", column = "id") String userId
    ) {
        return userFriendService.getFriendCards(size, userId);
    }

    @GetMapping("/subscribers")
    @ResponseStatus(HttpStatus.OK)
    public List<UserInfoDtoForSubscribersPage> getSubscribers(
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size,
            @PathVariable @Exists(table = "user_entity", column = "id") String userId,
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "-1") Long countryId,
            @RequestParam(defaultValue = "-1") Long cityId
    ) {
        return userFriendService.getSubscribers(page, size, userId, firstName, lastName, countryId, cityId);
    }

    @GetMapping("/subscribers/cards")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDtoWrapperForUserCart getSubscriberCards(
            @RequestParam @NotNull @Positive Integer size,
            @PathVariable @Exists(table = "user_entity", column = "id") String userId
    ) {
        return userFriendService.getSubscriberCards(size, userId);
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
