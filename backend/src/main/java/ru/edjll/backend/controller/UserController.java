package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.EditUserInfoDto;
import ru.edjll.backend.dto.UserInfoDetailDto;
import ru.edjll.backend.dto.UserInfoDto;
import ru.edjll.backend.dto.user.UserDtoWrapperForSave;
import ru.edjll.backend.service.UserInfoService;
import ru.edjll.backend.service.UserService;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserInfoService userInfoService;
    private final UserService userService;

    public UserController(UserInfoService userInfoService, UserService userService) {
        this.userInfoService = userInfoService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid UserDtoWrapperForSave userDtoWrapperForSave) {
        userService.register(userDtoWrapperForSave);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update")
    public void update(
            @RequestBody EditUserInfoDto editUserInfoDto,
            @AuthenticationPrincipal Principal principal
    ) {
        userInfoService.update(editUserInfoDto, principal);
    }

    @GetMapping("/{username}")
    public UserInfoDto getUserInfo(@PathVariable String username) {
        return userInfoService.getUserInfoByUsername(username);
    }

    @GetMapping("/search")
    public Collection<UserInfoDto> searchUsers(
            @RequestParam(required = false, defaultValue = "") String firstName,
            @RequestParam(required = false, defaultValue = "") String lastName,
            @RequestParam(required = false) Optional<Long> countryId,
            @RequestParam(required = false) Optional<Long> cityId
    ) {
        return userInfoService.searchUserInfo(firstName, lastName, countryId, cityId);
    }

    @GetMapping("/{username}/detail")
    public UserInfoDetailDto getUserInfoDetail(@PathVariable(required = false) String username) {
        return userInfoService.getUserInfoDetailByUsername(username);
    }
}
