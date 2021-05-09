package ru.edjll.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.group.GroupDtoForSearch;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.user.InterlocutorDto;
import ru.edjll.backend.dto.user.UserDtoWrapperForSave;
import ru.edjll.backend.dto.user.UserDtoWrapperForUpdate;
import ru.edjll.backend.dto.user.UserGroupsDto;
import ru.edjll.backend.dto.user.info.UserInfoDetailDto;
import ru.edjll.backend.dto.user.info.UserInfoDto;
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
import java.util.List;
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
    public List<UserInfoDtoForSearch> get(
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size,
            @RequestParam(defaultValue = "") String firstName,
            @RequestParam(defaultValue = "") String lastName,
            @RequestParam(defaultValue = "-1") Long countryId,
            @RequestParam(defaultValue = "-1") Long cityId,
            Principal principal
    ) {
        return userService.searchUserInfo(page, size, firstName, lastName, countryId, cityId, Optional.ofNullable(principal));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDto getById(
            @PathVariable @NotEmpty String id
    ) {
        return userInfoService.getUserInfoDtoById(id);
    }

    @GetMapping("/username/{username}")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDto getByUsername(
            @PathVariable @NotEmpty String username
    ) {
        return userInfoService.getUserInfoByUsername(username);
    }

    @GetMapping("/{id}/details")
    @ResponseStatus(HttpStatus.OK)
    public UserInfoDetailDto getDetails(
            @PathVariable @NotEmpty @Exists(table = "user_entity", column = "id") String id
    ) {
        return userInfoService.getUserInfoDetailById(id);
    }

    @GetMapping("/{id}/groups")
    @ResponseStatus(HttpStatus.OK)
    public List<GroupDtoForSearch> getGroups(
            @PathVariable @Exists(table = "user_entity", column = "id") String id,
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size,
            Principal principal
    ) {
        return groupService.getDtoByUserId(id, Optional.ofNullable(principal), page, size);
    }

    @GetMapping("/{id}/groups/cards")
    @ResponseStatus(HttpStatus.OK)
    public UserGroupsDto getUserGroups(
            @PathVariable @Exists(table = "user_entity", column = "id") String id,
            @RequestParam @NotNull @Positive Integer size
    ) {
        return groupService.getCardDtoByUserId(id, size);
    }

    @GetMapping("/interlocutors")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public List<InterlocutorDto> getInterlocutors(
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size,
            Principal principal
    ) {
        return userService.getInterlocutors(principal, page, size);
    }

    @GetMapping("/interlocutors/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public InterlocutorDto getInterlocutor(
            @PathVariable String id,
            Principal principal
    ) {
        return userService.getInterlocutor(id, principal);
    }

    @GetMapping("/{id}/feed")
    @ResponseStatus(HttpStatus.OK)
    public List<PostDto> getFeed(
            @PathVariable @Exists(table = "user_entity", column = "id") String id,
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size
    ) {
        return userService.getFeed(id, page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @RequestBody @Valid UserDtoWrapperForSave userDtoWrapperForSave
    ) {
        userService.register(userDtoWrapperForSave);
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @RequestBody @Valid UserDtoWrapperForUpdate userDtoWrapperForUpdate,
            Principal principal
    ) {
        userService.update(userDtoWrapperForUpdate, principal);
    }
}
