package ru.edjll.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.user.info.UserInfoDetailDto;
import ru.edjll.backend.dto.user.info.UserInfoDto;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSave;
import ru.edjll.backend.entity.UserInfo;
import ru.edjll.backend.exception.ResponseParameterException;
import ru.edjll.backend.repository.UserInfoRepository;

import java.util.Optional;

@Service
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserService userService;
    private final CityService cityService;

    public UserInfoService(UserInfoRepository userInfoRepository, UserService userService, CityService cityService) {
        this.userInfoRepository = userInfoRepository;
        this.userService = userService;
        this.cityService = cityService;
    }

    public void save(UserInfoDtoForSave userInfoDtoForSave, String username) {
        if (userInfoDtoForSave.getCityId() != null || userInfoDtoForSave.getBirthday() != null) {
            userService.getUserByUsername(username).ifPresent(user -> {
                UserInfo userInfo = userInfoDtoForSave.toUserInfo();
                userInfo.setUser(user);
                userInfoRepository.save(userInfo);
            });
        }
    }

    public void update(UserInfoDtoForSave userInfoDtoForSave, String id) {
        UserInfo userInfo = this.getUserInfoById(id)
                .orElseGet(() -> {
                    UserInfo uf = new UserInfo();
                    userService.getUserById(id).ifPresent(uf::setUser);
                    return uf;
                });

        userInfo.setBirthday(userInfoDtoForSave.getBirthday());

        if (userInfo.getCity() == null || !userInfo.getCity().getId().equals(userInfoDtoForSave.getCityId())) {
            if (userInfoDtoForSave.getCityId() != null) {
                Optional.ofNullable(cityService.getById(userInfoDtoForSave.getCityId()))
                        .ifPresent(userInfo::setCity);
            } else {
                userInfo.setCity(null);
            }
        }
        userInfoRepository.save(userInfo);
    }

    public Optional<UserInfo> getUserInfoById(String id) {
        return userInfoRepository.findById(id);
    }

    public UserInfoDto getUserInfoByUsername(String username) {
        return userInfoRepository.getUserInfoByUsername(username)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "username", username, "exists"));
    }

    public UserInfoDto getUserInfoDtoById(String id) {
        return userInfoRepository.getUserInfoById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id, "exists"));
    }

    public UserInfoDetailDto getUserInfoDetailById(String id) {
        return userInfoRepository.getUserInfoDetailById(id);
    }
}
