package ru.edjll.backend.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.group.*;
import ru.edjll.backend.dto.user.UserGroupsDto;
import ru.edjll.backend.entity.Group;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.exception.ResponseParameterException;
import ru.edjll.backend.repository.GroupRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final GroupUserService groupUserService;

    public GroupService(GroupRepository groupRepository, UserService userService, @Lazy GroupUserService groupUserService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.groupUserService = groupUserService;
    }

    public GroupDto getDtoByAddress(String address) {
        return groupRepository.getDtoByAddress(address)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "address", address, "exists"));
    }

    public void save(GroupDtoForSave groupDtoForSave, Principal principal) {
        User creator = userService.getUserById(principal.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Group group = groupDtoForSave.toGroup();
        group.setCreator(creator);
        groupRepository.save(group);
        groupUserService.subscribe(group.getId(), principal);
    }

    public void update(Long id, GroupDtoForUpdate groupDtoForUpdate, Principal principal) {
        Group groupFromDB = groupRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));

        if (!groupFromDB.getAddress().equals(groupDtoForUpdate.getAddress())
                && groupRepository.existsByAddressAndIdNot(groupDtoForUpdate.getAddress(), groupFromDB.getId())
        ) {
            throw new ResponseParameterException(HttpStatus.BAD_REQUEST, "address", groupDtoForUpdate.getAddress(), "unique");
        }

        if (!groupFromDB.getCreator().getId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        groupFromDB.setTitle(groupDtoForUpdate.getTitle());
        groupFromDB.setAddress(groupDtoForUpdate.getAddress());
        groupFromDB.setDescription(groupDtoForUpdate.getDescription());

        groupRepository.save(groupFromDB);
    }

    public void delete(Long id, JwtAuthenticationToken principal) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));

        if (!principal.getName().equals(group.getCreator().getId()) && principal.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new ResponseParameterException(HttpStatus.FORBIDDEN, "user", principal.getName(), "forbidden");
        }

        groupUserService.deleteAllByIdGroupId(group.getId());
        groupRepository.deleteById(group.getId());
    }

    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    public List<GroupDtoForSearch> getDtoByUserId(String id, Optional<Principal> principal, Integer page, Integer size) {
        return principal
                .map(pr -> groupRepository.getDtoByUserId(id, pr.getName(), PageRequest.of(page, size)))
                .orElseGet(() -> groupRepository.getDtoByUserId(id, PageRequest.of(page, size)));
    }

    public List<GroupDtoForSearch> getAll(Integer page, Integer size, Optional<Principal> principal) {
        return principal
                .map(user -> groupRepository.getAll(user.getName(), PageRequest.of(page, size)))
                .orElseGet(() -> groupRepository.getAll(PageRequest.of(page, size)));
    }

    public UserGroupsDto getCardDtoByUserId(String userId, Integer size) {
        List<GroupDtoForSearch> groups = groupRepository.getDtoByUserId(userId, PageRequest.of(0, size));
        int count = groupUserService.countGroupsByUserId(userId);

        return new UserGroupsDto(groups, count);
    }

    public Page<GroupDtoForAdminPage> getAllForAdmin(Integer page, Integer size) {
        return groupRepository.getAllForAdmin(PageRequest.of(page, size));
    }

    public void update(Long id, GroupDtoForAdminUpdate groupDtoForAdminUpdate) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));
        group.setEnabled(groupDtoForAdminUpdate.getEnabled());
        groupRepository.save(group);
    }
}
