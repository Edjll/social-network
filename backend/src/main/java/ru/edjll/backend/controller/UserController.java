package ru.edjll.backend.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.group.GroupDtoForSearch;
import ru.edjll.backend.dto.group.GroupDtoForUserPage;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.user.UserDtoWrapperForSave;
import ru.edjll.backend.dto.user.info.UserInfoDetailDto;
import ru.edjll.backend.dto.user.info.UserInfoDto;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSave;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;
import ru.edjll.backend.service.GroupService;
import ru.edjll.backend.service.UserInfoService;
import ru.edjll.backend.service.UserService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserInfoService userInfoService;
    private final UserService userService;
    private final GroupService groupService;

    public UserController(UserInfoService userInfoService, UserService userService, GroupService groupService) {
        this.userInfoService = userInfoService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UserInfoDtoForSearch> get(
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

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<UserInfoDto> getByUsername(
            @PathVariable
            @NotEmpty(message = "{user.username.notEmpty}")
            @Exists(table = "user_entity", column = "username", message = "{user.username.exists}") String username) {
        return userInfoService.getUserInfoByUsername(username);
    }

    @GetMapping("/{id}/details")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDetailDto getDetails(
            @PathVariable
            @NotEmpty(message = "{user.username.notEmpty}") String id
    ) {
        return userInfoService.getUserInfoDetailById(id);
    }

    @GetMapping("/{id}/groups")
    @ResponseStatus(HttpStatus.OK)
    public Page<GroupDtoForSearch> getGroups(
            @PathVariable
            @NotEmpty
            @Exists(table = "user_entity", column = "id") String id,
            @RequestParam
            @NotNull
            @PositiveOrZero Integer page,
            @RequestParam
            @NotNull
            @Positive Integer pageSize,
            Principal principal
    ) {
        return groupService.getDtoByUserId(id, Optional.ofNullable(principal), page, pageSize);
    }

    @GetMapping("/{id}/feed")
    @ResponseStatus(HttpStatus.OK)
    public Page<PostDto> getFeed(
            @PathVariable
            @NotEmpty
            @Exists(table = "user_entity", column = "id") String id,
            @RequestParam
            @NotNull
            @PositiveOrZero Integer page,
            @RequestParam
            @NotNull
            @Positive Integer size
    ) {
        return userService.getFeed(id, page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid UserDtoWrapperForSave userDtoWrapperForSave) {
        userService.register(userDtoWrapperForSave);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @RequestBody @Valid UserInfoDtoForSave userInfoDtoForSave,
            @AuthenticationPrincipal Principal principal
    ) {
        userInfoService.update(userInfoDtoForSave, principal);
    }
}
