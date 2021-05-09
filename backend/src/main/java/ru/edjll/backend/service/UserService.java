package ru.edjll.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.user.*;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.exception.ResponseParameterException;
import ru.edjll.backend.repository.UserRepository;

import java.security.Principal;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final UserInfoService userInfoService;

    @Value("${keycloak.admin.url}")
    private String keycloakUrl;

    public UserService(UserRepository userRepository, RestTemplate restTemplate, AuthService authService, @Lazy UserInfoService userInfoService) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.userInfoService = userInfoService;
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void register(UserDtoWrapperForSave userDtoWrapperForSave) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + authService.getAdminToken());

        restTemplate.exchange(keycloakUrl + "/users", HttpMethod.POST, new HttpEntity<>(userDtoWrapperForSave.toUserDtoForSave(), httpHeaders), Object.class);

        userInfoService.save(userDtoWrapperForSave.toUserInfoDtoForSave(), userDtoWrapperForSave.getUsername());
    }

    public void changeEnabled(String id, UserDtoForChangeEnabled userDtoForChangeEnabled) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + authService.getAdminToken());

        restTemplate.exchange(keycloakUrl + "/users/" + id, HttpMethod.PUT, new HttpEntity<>(userDtoForChangeEnabled, httpHeaders), Object.class);
    }

    public Page<UserDtoForAdminPage> getAll(
            Integer page, Integer size,
            String idDirection, String usernameDirection, String emailDirection, String cityDirection, String enabledDirection,
            String id, String username, String email, String city
    ) {
        return userRepository.getUsersForAdmin(id, username, email, city, idDirection, usernameDirection, emailDirection, cityDirection, enabledDirection, PageRequest.of(page, size));
    }

    public List<PostDto> getFeed(String id, Integer page, Integer size) {
        return userRepository.getFeed(id, PageRequest.of(page, size));
    }

    public List<InterlocutorDto> getInterlocutors(Principal principal, Integer page, Integer size) {
        return userRepository.getInterlocutors(principal.getName(), PageRequest.of(page, size));
    }

    public InterlocutorDto getInterlocutor(String id, Principal principal) {
        return userRepository.getInterlocutor(principal.getName(), id);
    }

    public void update(UserDtoWrapperForUpdate userDtoWrapperForUpdate, Principal principal) {
        User user = getUserById(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        if (!user.getEmail().equals(userDtoWrapperForUpdate.getEmail())
                && userRepository.existsByEmailAndIdNot(userDtoWrapperForUpdate.getEmail(), principal.getName())
        ) {
            throw new ResponseParameterException(HttpStatus.BAD_REQUEST, "email", userDtoWrapperForUpdate.getEmail(), "unique");
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + authService.getAdminToken());

        restTemplate.exchange(keycloakUrl + "/users/" + user.getId(), HttpMethod.PUT, new HttpEntity<>(userDtoWrapperForUpdate.toUserDtoForUpdate(), httpHeaders), Object.class);

        userInfoService.update(userDtoWrapperForUpdate.toUserInfoDtoForSave(), user.getId());
    }

    public List<UserInfoDtoForSearch> searchUserInfo(Integer page, Integer size, String firstName, String lastName, Long countryId, Long cityId, Optional<Principal> principal) {
        Pageable pageable = PageRequest.of(page, size);
        if (principal.isPresent()) {
            return userRepository.searchUsersByAuthorizedUser(principal.get().getName(), firstName, lastName, cityId, countryId, pageable);
        } else {
            return userRepository.searchUsersByAnonymousUser(firstName, lastName, cityId, countryId, pageable);
        }
    }
}
