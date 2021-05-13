package ru.edjll.backend.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.user.UserDtoForAdminPage;
import ru.edjll.backend.dto.user.UserDtoForChangeEnabled;
import ru.edjll.backend.service.UserService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Validated
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDtoForAdminPage> getAllForAdmin(
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size,
            @RequestParam(defaultValue = "") String idDirection,
            @RequestParam(defaultValue = "") String usernameDirection,
            @RequestParam(defaultValue = "") String emailDirection,
            @RequestParam(defaultValue = "") String cityDirection,
            @RequestParam(defaultValue = "") String enabledDirection,
            @RequestParam(defaultValue = "") String id,
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String city
    ) {
        return userService.getAllForAdmin(page, size, idDirection, usernameDirection, emailDirection, cityDirection, enabledDirection, id, username, email, city);
    }

    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public Integer getCountForAdmin(
            @RequestParam(defaultValue = "") String id,
            @RequestParam(defaultValue = "") String username,
            @RequestParam(defaultValue = "") String email,
            @RequestParam(defaultValue = "") String city
    ) {
        return userService.getCountForAdmin(id, username, email, city);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeEnabled(
            @PathVariable @Exists(table = "user_entity", column = "id") String id,
            @RequestBody @Valid UserDtoForChangeEnabled userDtoForChangeEnabled
    ) {
        userService.changeEnabled(id, userDtoForChangeEnabled);
    }
}
