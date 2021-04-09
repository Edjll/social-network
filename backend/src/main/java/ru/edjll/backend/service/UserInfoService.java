package ru.edjll.backend.service;

import org.springframework.data.domain.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.user.UserDtoForAdminPage;
import ru.edjll.backend.dto.userInfo.UserInfoDetailDto;
import ru.edjll.backend.dto.userInfo.UserInfoDto;
import ru.edjll.backend.dto.userInfo.UserInfoDtoForSave;
import ru.edjll.backend.dto.userInfo.UserInfoDtoForSearch;
import ru.edjll.backend.entity.UserInfo;
import ru.edjll.backend.repository.UserInfoRepository;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserInfoService {

    private final UserInfoRepository userInfoRepository;
    private final UserService userService;
    private final CityService cityService;
    private final JdbcTemplate jdbcTemplate;

    public UserInfoService(UserInfoRepository userInfoRepository, UserService userService, CityService cityService, JdbcTemplate jdbcTemplate) {
        this.userInfoRepository = userInfoRepository;
        this.userService = userService;
        this.cityService = cityService;
        this.jdbcTemplate = jdbcTemplate;
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

    public Optional<UserInfoDto> getUserInfoByUsername(String username) {
        return userInfoRepository.getUserInfoByUsername(username);
    }

    public Page<UserInfoDtoForSearch> searchUserInfo(Integer page, Integer size, Optional<String> firstName, Optional<String> lastName, Optional<Long> countryId, Optional<Long> cityId, Optional<Principal> principal) {
        Map<String, String> stringSearchParams = new HashMap<>();
        Map<String, Object> searchParams = new HashMap<>();

        String sqlSelect = "select user_entity.id, user_entity.username, user_entity.first_name, user_entity.last_name, city.title as city";
        String sqlFrom   = "from user_entity " +
                                "left join user_info on user_entity.id = user_info.user_id " +
                                "left join city on user_info.city_id = city.id";
        String sqlWhere  = "where realm_id = 'social-network' and service_account_client_link is null and user_entity.enabled = true";

        firstName.ifPresent(s -> stringSearchParams.put("first_name", s));
        lastName.ifPresent(s -> stringSearchParams.put("last_name", s));
        cityId.ifPresent(s -> searchParams.put("city.id", s));

        if (countryId.isPresent()) {
            sqlFrom += " join country on city.country_id = country.id";
            searchParams.put("country.id", countryId.get());
        }

        if (principal.isPresent()) {
            sqlFrom += " left join user_friend on user_friend.friend_id = '" + principal.get().getName() + "' and user_friend.user_id = user_entity.id";
            sqlSelect += ", user_friend.status";
        } else {
            sqlSelect += ", null as status";
        }

        String countSql =   "select count(*) " + sqlFrom + " " + sqlWhere;

        if (!stringSearchParams.isEmpty()) {
            sqlWhere += stringSearchParams.entrySet().stream()
                    .filter(entry -> !entry.getValue().isEmpty())
                    .map(entry -> "lower(" + entry.getKey() + ") like concat('%', lower('" + entry.getValue() + "'), '%')")
                    .reduce("", (acc, str) -> acc + " and " + str);
        }

        if (!searchParams.isEmpty()) {
            sqlWhere += searchParams.entrySet().stream()
                    .map(entry -> entry.getKey() + " = " + entry.getValue())
                    .reduce("", (acc, str) -> acc + " and " + str);
        }

        int count = jdbcTemplate.queryForObject(countSql, Integer.class);

        String sql = sqlSelect + " " + sqlFrom + " " + sqlWhere + " limit " + size + " offset " + page * size;

        List<UserInfoDtoForSearch> users = jdbcTemplate.query(sql, (rs, rowNumber) -> new UserInfoDtoForSearch(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("city"),
                (Integer) rs.getObject("status")
        ));

        return new PageImpl<>(users, PageRequest.of(page, size), count);
    }

    public UserInfoDetailDto getUserInfoDetailByUsername(String username) {
        return userInfoRepository.getUserInfoDetailByUsername(username);
    }
}
