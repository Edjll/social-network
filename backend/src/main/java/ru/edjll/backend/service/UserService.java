package ru.edjll.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.UserInfoDetailDto;
import ru.edjll.backend.dto.user.UserDtoForSave;
import ru.edjll.backend.dto.user.UserDtoWrapperForSave;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.repository.UserRepository;

import javax.transaction.Transactional;
import javax.xml.ws.Response;
import java.util.Map;
import java.util.Optional;

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
}
