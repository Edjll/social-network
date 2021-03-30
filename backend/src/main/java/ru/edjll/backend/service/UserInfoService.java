package ru.edjll.backend.service;

import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.EditUserInfoDto;
import ru.edjll.backend.dto.UserInfoDetailDto;
import ru.edjll.backend.dto.UserInfoDto;
import ru.edjll.backend.entity.City;
import ru.edjll.backend.entity.UserInfo;
import ru.edjll.backend.repository.UserInfoRepository;

import java.security.Principal;
import java.util.Collection;

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

    public void update(EditUserInfoDto editUserInfoDto, Principal principal) {
        UserInfo userInfo = this.getUserInfoById(principal.getName());
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUser(userService.getUserById(principal.getName()));
        }
        userInfo.setBirthday(editUserInfoDto.getBirthday());
        if (userInfo.getCity() == null || !userInfo.getCity().getId().equals(editUserInfoDto.getCityId())) {
            if (editUserInfoDto.getCityId() == null) userInfo.setCity(null);
            else {
                City city = cityService.getById(editUserInfoDto.getCityId());
                userInfo.setCity(city);
            }
        }
        userInfoRepository.save(userInfo);
    }

    public UserInfo getUserInfoById(String id) {
        return userInfoRepository.findById(id).orElse(null);
    }

    public UserInfoDto getUserInfoByUsername(String username) {
        return userInfoRepository.getUserInfoByUsername(username);
    }

    public Collection<UserInfoDto> searchUserInfo(String firstName, String lastName, Long countryId, Long cityId) {
        if (cityId != null) return userInfoRepository.getUserInfoByFirstNameAndLastNameAndCityId(firstName, lastName, cityId);
        if (countryId != null) return userInfoRepository.getUserInfoByFirstNameAndLastNameAndCountryId(firstName, lastName, countryId);
        return userInfoRepository.getUserInfoByFirstNameAndLastName(firstName, lastName);
    }

    public UserInfoDetailDto getUserInfoDetailByUsername(String id) {
        return userInfoRepository.getUserInfoDetailByUsername(id);
    }
}
