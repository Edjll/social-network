package ru.edjll.backend.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.group.GroupDtoForAdminPage;
import ru.edjll.backend.dto.group.GroupDtoForAdminUpdate;
import ru.edjll.backend.service.GroupService;

@RestController
@RequestMapping("/admin/groups")
public class AdminGroupController {

    private final GroupService groupService;

    public AdminGroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public Page<GroupDtoForAdminPage> get(
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        return groupService.getAllForAdmin(page, size);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody GroupDtoForAdminUpdate groupDtoForAdminUpdate) {
        groupService.update(id, groupDtoForAdminUpdate);
    }
}
