package ru.edjll.backend.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.group.*;
import ru.edjll.backend.entity.Group;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.repository.GroupRepository;

import java.security.Principal;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Group with address '" + address + "' not found"));
    }

    public void save(GroupDtoForSave groupDtoForSave, Principal principal) {
        User creator = userService.getUserById(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Group group = groupDtoForSave.toGroup();
        group.setCreator(creator);
        groupRepository.save(group);
        groupUserService.subscribe(group.getId(), principal);
    }

    public void update(Long id, GroupDtoForUpdate groupDtoForUpdate, Principal principal) {
        Group groupFromDB = groupRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!groupFromDB.getCreator().getId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        groupFromDB.setTitle(groupDtoForUpdate.getTitle());
        groupFromDB.setAddress(groupDtoForUpdate.getAddress());
        groupFromDB.setDescription(groupDtoForUpdate.getDescription());

        groupRepository.save(groupFromDB);
    }

    public void delete(Long id, JwtAuthenticationToken principal) {
        Group group = groupRepository.getOne(id);

        if (!principal.getName().equals(group.getCreator().getId()) && principal.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        groupUserService.deleteAllByIdGroupId(group.getId());
        groupRepository.deleteById(group.getId());
    }

    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    public Optional<String> findCreatorIdByGroupId(Long id) {
        return groupRepository.findCreatorIdByGroupId(id);
    }

    public Page<GroupDtoForSearch> getDtoByUserId(String id, Optional<Principal> principal, Integer page, Integer pageSize) {
        return principal
                .map(pr -> groupRepository.getDtoByUserId(id, pr.getName(), PageRequest.of(page, pageSize)))
                .orElseGet(() -> groupRepository.getDtoByUserId(id, PageRequest.of(page, pageSize)));
    }

    public Page<GroupDtoForSearch> getAll(Integer page, Integer pageSize, Optional<Principal> principal) {
        return principal
                .map(user -> groupRepository.getAll(user.getName(), PageRequest.of(page, pageSize)))
                .orElseGet(() -> groupRepository.getAll(PageRequest.of(page, pageSize)));
    }

    public Page<GroupDtoForAdminPage> getAllForAdmin(Integer page, Integer size) {
        return groupRepository.getAllForAdmin(PageRequest.of(page, size));
    }

    public void update(Long id, GroupDtoForAdminUpdate groupDtoForAdminUpdate) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        group.setEnabled(groupDtoForAdminUpdate.getEnabled());
        groupRepository.save(group);
    }
}
