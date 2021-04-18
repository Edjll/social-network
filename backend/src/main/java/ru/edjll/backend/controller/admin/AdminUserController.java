package ru.edjll.backend.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.user.UserDtoForAdminPage;
import ru.edjll.backend.dto.user.UserDtoForChangeEnabled;
import ru.edjll.backend.service.UserService;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
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

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeEnabled(
            @PathVariable
            @NotEmpty(message = "{user.id.notEmpty}")
            @Exists(table = "user_entity", column = "id", message = "{user.id.exists}") String id,
            @RequestBody
            @Valid UserDtoForChangeEnabled userDtoForChangeEnabled
    ) {
        userService.changeEnabled(id, userDtoForChangeEnabled);
    }
}