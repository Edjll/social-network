package ru.edjll.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.userFriend.UserFriendDtoForDelete;
import ru.edjll.backend.dto.userFriend.UserFriendDtoForSave;
import ru.edjll.backend.dto.userFriend.UserFriendDtoForUpdate;
import ru.edjll.backend.dto.userInfo.UserInfoDtoForFriendsPage;
import ru.edjll.backend.repository.UserFriendRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserFriendService {

    private final UserFriendRepository userFriendRepository;
    private final JdbcTemplate jdbcTemplate;

    public UserFriendService(UserFriendRepository userFriendRepository, JdbcTemplate jdbcTemplate) {
        this.userFriendRepository = userFriendRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(UserFriendDtoForSave userFriendDtoForSave) {
        this.userFriendRepository.save(userFriendDtoForSave.toUserFriend());
    }

    public void update(UserFriendDtoForUpdate userFriendDtoForUpdate) {
        this.userFriendRepository.save(userFriendDtoForUpdate.toUserFriend());
    }

    public void delete(UserFriendDtoForDelete userFriendDtoForDelete) {
        this.userFriendRepository.deleteById(userFriendDtoForDelete.toUserFriendKey());
    }

    public Page<UserInfoDtoForFriendsPage> getUsers(Integer page, Integer size, String userId, Optional<String> firstName, Optional<String> lastName, Optional<Long> countryId, Optional<Long> cityId) {
        Map<String, String> stringSearchParams = new HashMap<>();
        Map<String, Object> searchParams = new HashMap<>();

        String sqlSelect = "select user_entity.id, user_entity.username, user_entity.first_name, user_entity.last_name, city.title as city";
        String sqlFrom = "from user_friend " +
                "join user_entity on user_friend.user_id = '" + userId + "' and user_friend.friend_id = user_entity.id " +
                "left join user_info on user_entity.id = user_info.user_id " +
                "left join city on user_info.city_id = city.id";
        String sqlWhere = "where realm_id = 'social-network' and service_account_client_link is null and user_entity.enabled = true";

        firstName.ifPresent(s -> stringSearchParams.put("first_name", s));
        lastName.ifPresent(s -> stringSearchParams.put("last_name", s));
        cityId.ifPresent(s -> searchParams.put("city.id", s));

        if (countryId.isPresent()) {
            sqlFrom += " join country on city.country_id = country.id";
            searchParams.put("country.id", countryId.get());
        }

        String countSql = "select count(*) " + sqlFrom + " " + sqlWhere;

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

        List<UserInfoDtoForFriendsPage> users = jdbcTemplate.query(sql, (rs, rowNumber) -> new UserInfoDtoForFriendsPage(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("city")
        ));

        return new PageImpl<>(users, PageRequest.of(page, size), count);
    }
}
