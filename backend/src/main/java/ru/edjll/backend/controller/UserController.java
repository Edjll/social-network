package ru.edjll.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.user.UserDtoForAdminPage;
import ru.edjll.backend.dto.user.UserDtoForChangeEnabled;
import ru.edjll.backend.dto.user.UserDtoWrapperForSave;
import ru.edjll.backend.dto.user.info.UserInfoDetailDto;
import ru.edjll.backend.dto.user.info.UserInfoDto;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSave;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;
import ru.edjll.backend.service.UserInfoService;
import ru.edjll.backend.service.UserService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Validated
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @RequestBody @Valid UserInfoDtoForSave userInfoDtoForSave,
            @AuthenticationPrincipal Principal principal
    ) {
        userInfoService.update(userInfoDtoForSave, principal);
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<UserInfoDto> getUserInfo(
            @PathVariable
            @NotEmpty(message = "{user.username.notEmpty}")
            @Exists(table = "user_entity", column = "username", message = "{user.username.exists}") String username) {
        return userInfoService.getUserInfoByUsername(username);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserInfoDtoForSearch> searchUsers(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam Optional<String> firstName,
            @RequestParam Optional<String> lastName,
            @RequestParam Optional<Long> countryId,
            @RequestParam Optional<Long> cityId,
            Principal principal
    ) {
        return userInfoService.searchUserInfo(page, size, firstName, lastName, countryId, cityId, Optional.ofNullable(principal));
    }

    @GetMapping("/{username}/detail")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDetailDto getUserInfoDetail(@PathVariable @NotEmpty(message = "{user.username.notEmpty}") String username) {
        return userInfoService.getUserInfoDetailByUsername(username);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/enabled")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeEnabled(@RequestBody @Valid UserDtoForChangeEnabled userDtoForChangeEnabled) {
        userService.changeEnabled(userDtoForChangeEnabled);
    }

    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserDtoForAdminPage> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam(required = false) Optional<String> idDirection,
            @RequestParam(required = false) Optional<String> usernameDirection,
            @RequestParam(required = false) Optional<String> emailDirection,
            @RequestParam(required = false) Optional<String> cityDirection,
            @RequestParam(required = false) Optional<String> enabledDirection,
            @RequestParam(required = false) Optional<String> id,
            @RequestParam(required = false) Optional<String> username,
            @RequestParam(required = false) Optional<String> email,
            @RequestParam(required = false) Optional<String> city
    ) {
        return userService.getAll(page, size, idDirection, usernameDirection, emailDirection, cityDirection, enabledDirection, id, username, email, city);
    }
}
