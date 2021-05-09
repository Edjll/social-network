package ru.edjll.backend.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.user.friend.UserFriendDtoForSave;
import ru.edjll.backend.dto.user.friend.UserFriendDtoForUpdate;
import ru.edjll.backend.dto.user.friend.UserFriendStatusDto;
import ru.edjll.backend.dto.user.info.UserInfoDtoForFriendsPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSubscribersPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForUserCart;
import ru.edjll.backend.dto.user.info.UserInfoDtoWrapperForUserCart;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.entity.UserFriend;
import ru.edjll.backend.entity.UserFriendKey;
import ru.edjll.backend.entity.UserFriendStatus;
import ru.edjll.backend.exception.ResponseParameterException;
import ru.edjll.backend.repository.UserFriendRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserFriendService {

    private final UserService userService;
    private final UserFriendRepository userFriendRepository;
    private final JdbcTemplate jdbcTemplate;

    public UserFriendService(UserService userService, UserFriendRepository userFriendRepository, JdbcTemplate jdbcTemplate) {
        this.userService = userService;
        this.userFriendRepository = userFriendRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(String userId, Principal principal) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "userId", userId, "exists"));
        User friend = new User();

        friend.setId(principal.getName());

        UserFriend userFriend = new UserFriend();
        userFriend.setStatus(UserFriendStatus.SUBSCRIBER);
        userFriend.setDate(LocalDateTime.now());
        userFriend.setId(new UserFriendKey(user, friend));

        this.userFriendRepository.save(userFriend);
    }

    public void update(String userId, Principal principal) {
        User friend = userService.getUserById(userId)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "userId", userId, "exists"));
        User user = new User();
        user.setId(principal.getName());

        UserFriend userFriend = userFriendRepository
                .findById(new UserFriendKey(user, friend))
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", "(userId, " + principal.getName() + ")", "exists"));

        if (userFriend.getStatus().equals(UserFriendStatus.FRIEND)) return;

        userFriend.setStatus(UserFriendStatus.FRIEND);
        userFriend.setDate(LocalDateTime.now());

        this.userFriendRepository.save(userFriend);
    }

    public void delete(String userId, Principal principal) {
        this.userFriendRepository.deleteByUserIdAndFriendId(userId, principal.getName());
    }

    public List<UserInfoDtoForFriendsPage> getFriends(Integer page, Integer size, String userId, String firstName, String lastName, Long countryId, Long cityId) {
        return userFriendRepository.getAllFriendsDto(userId, firstName, lastName, cityId, countryId, PageRequest.of(page, size));
    }

    public UserInfoDtoWrapperForUserCart getFriendCards(Integer size, String userId) {
        List<UserInfoDtoForUserCart> users = userFriendRepository.getAllFriendCardsDto(userId, PageRequest.of(0, size));
        int count = userFriendRepository.countByIdUserIdAndStatusOrIdFriendIdAndStatus(userId, UserFriendStatus.FRIEND, userId, UserFriendStatus.FRIEND);

        return new UserInfoDtoWrapperForUserCart(users, count);
    }

    public UserFriendStatusDto friendshipExists(String userId, String friendId) {
        return userFriendRepository.friendshipExists(userId, friendId);
    }

    public List<UserInfoDtoForFriendsPage> getSubscribers(Integer page, Integer size, String userId, String firstName, String lastName, Long countryId, Long cityId) {
        return userFriendRepository.getAllSubscribersDto(userId, firstName, lastName, cityId, countryId, PageRequest.of(page, size));
    }

    public UserInfoDtoWrapperForUserCart getSubscriberCards(Integer size, String userId) {
        List<UserInfoDtoForUserCart> users = userFriendRepository.getAllSubscriberCardsDto(userId, PageRequest.of(0, size));
        int count = userFriendRepository.countByIdUserIdAndStatus(userId, UserFriendStatus.SUBSCRIBER);

        return new UserInfoDtoWrapperForUserCart(users, count);
    }
}
