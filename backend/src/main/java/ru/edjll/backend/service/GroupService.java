package ru.edjll.backend.service;

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

    public GroupService(GroupRepository groupRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }

    public Optional<GroupDto> getDtoByAddress(String address) {
        return groupRepository.getDtoByAddress(address);
    }

    public void save(GroupDtoForSave groupDtoForSave, Principal principal) {
        User creator = userService.getUserById(principal.getName()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
        Group group = groupDtoForSave.toGroup();
        group.setCreator(creator);
        groupRepository.save(group);
    }

    public void update(GroupDtoForUpdate groupDtoForUpdate, Principal principal) {
        Group groupFromDB = groupRepository.getOne(groupDtoForUpdate.getId());

        if (!groupFromDB.getCreator().getId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Group group = groupDtoForUpdate.toGroup();

        group.setCreator(groupFromDB.getCreator());
        group.setCreatedDate(groupFromDB.getCreatedDate());
        group.setEnabled(groupFromDB.getEnabled());

        groupRepository.save(group);
    }

    public void delete(Long id, JwtAuthenticationToken principal) {
        Group group = groupRepository.getOne(id);

        if (!principal.getName().equals(group.getCreator().getId()) && principal.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        groupRepository.deleteById(group.getId());
    }

    public Optional<Group> findById(Long id) {
        return groupRepository.findById(id);
    }

    public Optional<String> findCreatorIdByGroupId(Long id) {
        return groupRepository.findCreatorIdByGroupId(id);
    }

    public Page<GroupDtoForUserPage> getDtoByUserId(String userId, Integer page, Integer pageSize) {
        return groupRepository.getDtoByUserId(userId, PageRequest.of(page, pageSize));
    }

    public Page<GroupDtoForSearch> getAll(Integer page, Integer pageSize) {
        return groupRepository.getAll(PageRequest.of(page, pageSize));
    }
}
