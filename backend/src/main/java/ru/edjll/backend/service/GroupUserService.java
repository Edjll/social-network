package ru.edjll.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.group.user.GroupUserDtoForGroupPage;
import ru.edjll.backend.dto.group.user.GroupUserDtoForSubscribe;
import ru.edjll.backend.dto.group.user.GroupUserDtoForSubscribersPage;
import ru.edjll.backend.entity.Group;
import ru.edjll.backend.entity.GroupUser;
import ru.edjll.backend.entity.GroupUserKey;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.repository.GroupUserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GroupUserService {

    private final GroupUserRepository groupUserRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final JdbcTemplate jdbcTemplate;

    public GroupUserService(GroupUserRepository groupUserRepository, GroupService groupService, UserService userService, JdbcTemplate jdbcTemplate) {
        this.groupUserRepository = groupUserRepository;
        this.groupService = groupService;
        this.userService = userService;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void subscribe(GroupUserDtoForSubscribe groupUserDtoForSubscribe, Principal principal) {
        subscribe(groupUserDtoForSubscribe.getGroupId(), principal);
    }

    public void subscribe(Long groupId, Principal principal) {
        User user = new User();
        Group group = new Group();

        user.setId(principal.getName());
        group.setId(groupId);

        GroupUser groupUser = new GroupUser(group, user, LocalDateTime.now());

        groupUserRepository.save(groupUser);
    }

    public void unsubscribe(Long groupId, Principal principal) {
        Group group = new Group();
        User user = new User();

        group.setId(groupId);
        user.setId(principal.getName());

        groupUserRepository.deleteById(new GroupUserKey(group, user));
    }

    public Page<GroupUserDtoForGroupPage> getDtoByGroupId(Long groupId, Integer page, Integer pageSize) {
        return groupUserRepository.getDtoByGroupId(groupId, PageRequest.of(page, pageSize));
    }

    public Page<GroupUserDtoForSubscribersPage> getSubscribers(Integer page, Integer size, Long groupId, Optional<String> firstName, Optional<String> lastName, Optional<Long> countryId, Optional<Long> cityId) {
        Map<String, String> stringSearchParams = new HashMap<>();
        Map<String, Object> searchParams = new HashMap<>();

        String sqlSelect =  "select user_entity.id, user_entity.username, user_entity.first_name, user_entity.last_name, city.title as city";
        String sqlFrom =    "from group_user " +
                "join user_entity on user_entity.id = group_user.user_id " +
                "left join user_info on user_entity.id = user_info.user_id " +
                "left join city on user_info.city_id = city.id";
        String sqlWhere =   "where realm_id = 'social-network' and service_account_client_link is null and user_entity.enabled = true and group_user.group_id=" + groupId;

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

        List<GroupUserDtoForSubscribersPage> users = jdbcTemplate.query(sql, (rs, rowNumber) -> new GroupUserDtoForSubscribersPage(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("city")
        ));

        return new PageImpl<>(users, PageRequest.of(page, size), count);
    }
}
