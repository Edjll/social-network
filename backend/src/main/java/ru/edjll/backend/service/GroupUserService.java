package ru.edjll.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.edjll.backend.dto.group.user.GroupUserDtoForGroupPage;
import ru.edjll.backend.dto.group.user.GroupUserDtoForSubscribe;
import ru.edjll.backend.dto.group.user.GroupUserDtoForSubscribersPage;
import ru.edjll.backend.dto.group.user.GroupUserDtoWrapperForGroupPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;
import ru.edjll.backend.entity.Group;
import ru.edjll.backend.entity.GroupUser;
import ru.edjll.backend.entity.GroupUserKey;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.repository.GroupUserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class GroupUserService {

    private final GroupUserRepository groupUserRepository;

    public GroupUserService(GroupUserRepository groupUserRepository) {
        this.groupUserRepository = groupUserRepository;
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

    public List<UserInfoDtoForSearch> getSubscribers(Optional<Principal> principal, Integer page, Integer size, Long groupId, String firstName, String lastName, Long countryId, Long cityId) {
        Pageable pageable = PageRequest.of(page, size);
        if (principal.isPresent()) {
            return groupUserRepository.getAllDtoByAuthorizedUser(principal.get().getName(), groupId, firstName, lastName, cityId, countryId, pageable);
        } else {
            return groupUserRepository.getAllDtoByAnonymousUser(groupId, firstName, lastName, cityId, countryId, pageable);
        }
    }

    public GroupUserDtoWrapperForGroupPage getUsersWithUserByUserId(Long groupId, Optional<Principal> principal, Integer size) {
        List<GroupUserDtoForGroupPage> users;
        int count = groupUserRepository.countByIdGroupId(groupId);


        if (principal.isPresent()) {
            users = groupUserRepository.getCardDtoByAuthorizedUser(groupId, principal.get().getName(), size);
        } else {
            users = groupUserRepository.getCardDtoByAnonymousUser(groupId, PageRequest.of(0, size));
        }

        return new GroupUserDtoWrapperForGroupPage(users, count);
    }

    public void deleteAllByIdGroupId(Long groupId) {
        groupUserRepository.deleteAllByIdGroupId(groupId);
    }

    public int countGroupsByUserId(String userId) {
        return groupUserRepository.countByIdUserId(userId);
    }
}
