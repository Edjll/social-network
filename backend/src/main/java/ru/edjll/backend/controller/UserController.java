package ru.edjll.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.EditUserInfoDto;
import ru.edjll.backend.dto.UserInfoDetailDto;
import ru.edjll.backend.dto.UserInfoDto;
import ru.edjll.backend.service.UserInfoService;
import ru.edjll.backend.service.UserService;

import java.security.Principal;
import java.util.Collection;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserInfoService userInfoService;

    public UserController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/update")
    public ResponseEntity<Object> edit(@RequestBody EditUserInfoDto editUserInfoDto, @AuthenticationPrincipal Principal principal) {
        userInfoService.edit(editUserInfoDto, principal);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserInfoDto> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok().body(userInfoService.getUserInfoByUsername(username));
    }

    @GetMapping("/search")
    public ResponseEntity<Collection<UserInfoDto>> searchUsers(
            @RequestParam(required = false, defaultValue = "") String firstName,
            @RequestParam(required = false, defaultValue = "") String lastName,
            @RequestParam(required = false) Long countryId,
            @RequestParam(required = false) Long cityId
    ) {
        return ResponseEntity.ok().body(userInfoService.searchUserInfo(firstName, lastName, countryId, cityId));
    }

    @GetMapping("/detail/{username}")
    public ResponseEntity<UserInfoDetailDto> getUserInfoDetail(
            @PathVariable(required = false) String username
    ) {
        return ResponseEntity.ok().body(userInfoService.getUserInfoDetailByUsername(username));
    }
}
