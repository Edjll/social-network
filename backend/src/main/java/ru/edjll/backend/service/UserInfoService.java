package ru.edjll.backend.service;

import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.userInfo.UserInfoDetailDto;
import ru.edjll.backend.dto.userInfo.UserInfoDto;
import ru.edjll.backend.dto.userInfo.UserInfoDtoForSave;
import ru.edjll.backend.entity.UserInfo;
import ru.edjll.backend.repository.UserInfoRepository;

import java.security.Principal;
import java.util.Collection;
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

    public void update(UserInfoDtoForSave userInfoDtoForSave, Principal principal) {
        UserInfo userInfo = this.getUserInfoById(principal.getName())
                .orElseGet(() -> {
                    UserInfo uf = new UserInfo();
                    userService.getUserById(principal.getName()).ifPresent(uf::setUser);
                    return uf;
                });

        userInfo.setBirthday(userInfoDtoForSave.getBirthday());

        if (userInfo.getCity() == null || !userInfo.getCity().getId().equals(userInfoDtoForSave.getCityId())) {
            if (userInfoDtoForSave.getCityId() != null) {
                cityService.getById(userInfoDtoForSave.getCityId())
                        .ifPresent(userInfo::setCity);
            }
        }
        userInfoRepository.save(userInfo);
    }

    public Optional<UserInfo> getUserInfoById(String id) {
        return userInfoRepository.findById(id);
    }

    public UserInfoDto getUserInfoByUsername(String username) {
        return userInfoRepository.getUserInfoByUsername(username);
    }

    public Collection<UserInfoDto> searchUserInfo(String firstName, String lastName, Optional<Long> countryId, Optional<Long> cityId) {
        if (cityId.isPresent()) return userInfoRepository.getUserInfoByFirstNameAndLastNameAndCityId(firstName, lastName, cityId.get());
        if (countryId.isPresent()) return userInfoRepository.getUserInfoByFirstNameAndLastNameAndCountryId(firstName, lastName, countryId.get());
        return userInfoRepository.getUserInfoByFirstNameAndLastName(firstName, lastName);
    }

    public UserInfoDetailDto getUserInfoDetailByUsername(String username) {
        return userInfoRepository.getUserInfoDetailByUsername(username);
    }
}
