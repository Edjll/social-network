package ru.edjll.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.userFriend.UserFriendDtoForDelete;
import ru.edjll.backend.dto.userFriend.UserFriendDtoForSave;
import ru.edjll.backend.dto.userFriend.UserFriendDtoForUpdate;
import ru.edjll.backend.dto.userInfo.UserInfoDtoForFriendsPage;
import ru.edjll.backend.service.UserFriendService;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/user/friend")
public class UserFriendController {

    private final UserFriendService userFriendService;

    public UserFriendController(UserFriendService userFriendService) {
        this.userFriendService = userFriendService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserInfoDtoForFriendsPage> getUsers(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String userId,
            @RequestParam Optional<String> firstName,
            @RequestParam Optional<String> lastName,
            @RequestParam Optional<Long> countryId,
            @RequestParam Optional<Long> cityId
    ) {
        return userFriendService.getUsers(page, size, userId, firstName, lastName, countryId, cityId);
    }

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody @Valid UserFriendDtoForSave userFriendDtoForSave) {
        this.userFriendService.save(userFriendDtoForSave);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody @Valid UserFriendDtoForUpdate userFriendDtoForUpdate) {
        this.userFriendService.update(userFriendDtoForUpdate);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@RequestBody @Valid UserFriendDtoForDelete userFriendDtoForDelete) {
        this.userFriendService.delete(userFriendDtoForDelete);
    }
}
