package ru.edjll.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.post.PostType;
import ru.edjll.backend.dto.user.UserDtoForAdminPage;
import ru.edjll.backend.dto.user.UserDtoForChangeEnabled;
import ru.edjll.backend.dto.user.UserDtoWrapperForSave;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final AuthService authService;
    private final UserInfoService userInfoService;
    private final JdbcTemplate jdbcTemplate;

    @Value("${keycloak.admin.url}")
    private String keycloakUrl;

    public UserService(UserRepository userRepository, RestTemplate restTemplate, AuthService authService, @Lazy UserInfoService userInfoService, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.authService = authService;
        this.userInfoService = userInfoService;
        this.jdbcTemplate = jdbcTemplate;
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
            Optional<String> idDirection, Optional<String> usernameDirection, Optional<String> emailDirection, Optional<String> cityDirection, Optional<String> enabledDirection,
            Optional<String> id, Optional<String> username, Optional<String> email, Optional<String> city
    ) {
        List<Sort.Order> orders = new ArrayList<>();
        Map<String, String> searchParams = new HashMap<>();

        idDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "id")));
        usernameDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "username")));
        emailDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "email")));
        cityDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "city")));
        enabledDirection.ifPresent(s -> orders.add(new Sort.Order(Sort.Direction.fromString(s), "enabled")));

        id.ifPresent(s -> searchParams.put("user_entity.id", s));
        username.ifPresent(s -> searchParams.put("username", s));
        email.ifPresent(s -> searchParams.put("email", s));
        city.ifPresent(s -> searchParams.put("city.title", s));

        String sql =    "select user_entity.id, user_entity.username, user_entity.email, city.title as city, user_entity.enabled " +
                        "from user_entity " +
                            "left join user_info on user_entity.id = user_info.user_id " +
                            "left join city on user_info.city_id = city.id " +
                        "where realm_id = 'social-network' and service_account_client_link is null and user_entity.id != 'b65bfe43-77dd-44f1-8199-a9dfa3946da7'";

        String countSql =   "select count(*) " +
                            "from user_entity " +
                                "left join user_info on user_entity.id = user_info.user_id " +
                                "left join city on user_info.city_id = city.id " +
                            "where realm_id = 'social-network' and service_account_client_link is null and user_entity.id != 'b65bfe43-77dd-44f1-8199-a9dfa3946da7'";

        if (!searchParams.isEmpty()) {
            String tmp = searchParams.entrySet().stream()
                    .filter(entry -> !entry.getValue().isEmpty())
                    .map(entry -> "lower(" + entry.getKey() + ") like concat('%', lower('" + entry.getValue() + "'), '%')")
                    .reduce("", (acc, str) -> acc + " and " + str);

            sql += tmp;
            countSql += tmp;
        }

        int count = jdbcTemplate.queryForObject(countSql, Integer.class);

        if (!orders.isEmpty()) {
            sql = sql + " order by " + orders.stream()
                    .map(order -> order.getProperty() + " " + order.getDirection())
                    .collect(Collectors.joining(", "));
        }

        sql = sql + " limit " + size + " offset " + page * size;

        List<UserDtoForAdminPage> users = jdbcTemplate.query(sql, (rs, rowNumber) -> new UserDtoForAdminPage(
                rs.getString("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("city"),
                rs.getBoolean("enabled")
        ));

        return new PageImpl<>(users, PageRequest.of(page, size, Sort.by(orders)), count);
    }

    public Page<PostDto> getFeed(String id, Integer page, Integer size) {
        int count = jdbcTemplate.queryForObject(
                "select sum(value) " +
                    "from   (select count(*) as value " +
                            "from post " +
                                "join " +
                                    "(select user_friend.friend_id as id " +
                                    " from user_friend " +
                                    " where user_friend.user_id = '" + id + "' " +
                                    " union " +
                                    " select user_friend.user_id as id " +
                                    " from user_friend " +
                                    " where user_friend.friend_id = '" + id + "') as users " +
                                "on post.user_id = users.id " +
                            "union all " +
                            "select count(*) as value " +
                            "from group_post " +
                            "join group_user on group_user.user_id = '" + id + "' " +
                                "and group_user.group_id = group_post.group_id) as size",
                Integer.class
        );

        if (count == 0) return new PageImpl<>(Collections.EMPTY_LIST, PageRequest.of(page, size), 0);

        List<PostDto> posts = jdbcTemplate.query(
                "select post.id, post.text, post.created_date, post.modified_date, post.user_id as creator_id, concat(user_entity.first_name, ' ', user_entity.last_name) as name, user_entity.username as address, '" + PostType.USER + "' as type " +
                    "from post " +
                        "join " +
                            "(select user_friend.friend_id as id " +
                            " from user_friend " +
                            " where user_friend.user_id = '" + id + "' and user_friend.status = 0 " +
                            " union " +
                            " select user_friend.user_id as id " +
                            " from user_friend " +
                            " where user_friend.friend_id = '" + id + "') as users " +
                            "on post.user_id = users.id " +
                        "join user_entity on user_entity.id = post.user_id " +
                        "union all " +
                        "select group_post.id, group_post.text, group_post.created_date, group_post.modified_date, groups.creator_id as creator_id, groups.title as name, groups.address as address, '" + PostType.GROUP + "' as type " +
                        "from group_post " +
                            "join groups on group_post.group_id = groups.id " +
                            "join group_user on group_user.user_id = '" + id + "' " +
                                "and groups.id = group_user.group_id " +
                        "order by created_date desc " +
                        "limit " + size + " offset " + page * size,
                (rs, rowNumber) -> new PostDto(
                        rs.getLong("id"),
                        rs.getString("text"),
                        rs.getObject("created_date", LocalDateTime.class),
                        rs.getObject("modified_date", LocalDateTime.class),
                        rs.getString("creator_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        PostType.valueOf(rs.getString("type"))
                ));
        return new PageImpl<>(posts, PageRequest.of(page, size), count);
    }
}
