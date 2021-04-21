package ru.edjll.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.edjll.backend.dto.group.post.GroupPostDtoForGroupPage;
import ru.edjll.backend.dto.group.post.GroupPostDtoForSave;
import ru.edjll.backend.dto.group.post.GroupPostDtoForUpdate;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.entity.Group;
import ru.edjll.backend.entity.GroupPost;
import ru.edjll.backend.exception.ResponseParameterException;
import ru.edjll.backend.repository.GroupPostRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class GroupPostService {

    private final GroupPostRepository groupPostRepository;
    private final GroupService groupService;

    public GroupPostService(GroupPostRepository groupPostRepository, GroupService groupService) {
        this.groupPostRepository = groupPostRepository;
        this.groupService = groupService;
    }

    public Optional<PostDto> save(Long groupId, GroupPostDtoForSave groupPostDtoForSave, Principal principal) {
        Group group = groupService.findById(groupId)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "group", groupId.toString(), "exists"));

        String creatorId = group.getCreator().getId();

        if (!creatorId.equals(principal.getName())) {
            throw new ResponseParameterException(HttpStatus.FORBIDDEN, "user", principal.getName(), "forbidden");
        }

        GroupPost groupPost = groupPostDtoForSave.toGroupPost();
        groupPost.setGroup(group);

        GroupPost savedGroupPost = groupPostRepository.save(groupPost);

        return groupPostRepository.getDtoById(savedGroupPost.getId());
    }

    public Optional<PostDto> update(Long id, GroupPostDtoForUpdate groupPostDtoForUpdate, Principal principal) {
        GroupPost groupPostFromDB = groupPostRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));

        if (!groupPostFromDB.getGroup().getCreator().getId().equals(principal.getName())) {
            throw new ResponseParameterException(HttpStatus.FORBIDDEN, "user", principal.getName(), "forbidden");
        }

        groupPostFromDB.setText(groupPostDtoForUpdate.getText());
        groupPostFromDB.setModifiedDate(LocalDateTime.now());

        groupPostRepository.save(groupPostFromDB);

        return groupPostRepository.getDtoById(groupPostRepository.save(groupPostFromDB).getId());
    }

    public void delete(Long id, JwtAuthenticationToken principal) {
        GroupPost groupPostFromDB = groupPostRepository.findById(id)
                .orElseThrow(() -> new ResponseParameterException(HttpStatus.NOT_FOUND, "id", id.toString(), "exists"));

        if (!principal.getName().equals(groupPostFromDB.getGroup().getCreator().getId()) && principal.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new ResponseParameterException(HttpStatus.FORBIDDEN, "user", principal.getName(), "forbidden");
        }

        groupPostRepository.deleteById(groupPostFromDB.getId());
    }

    public Page<PostDto> getDtoByGroupId(Long groupId, Integer page, Integer size) {
        return groupPostRepository.getDtoByGroupId(groupId, PageRequest.of(page, size, Sort.by("createdDate").descending()));
    }
}
