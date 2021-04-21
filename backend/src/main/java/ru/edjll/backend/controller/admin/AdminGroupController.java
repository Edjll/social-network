package ru.edjll.backend.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.group.GroupDtoForAdminPage;
import ru.edjll.backend.dto.group.GroupDtoForAdminUpdate;
import ru.edjll.backend.service.GroupService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/admin/groups")
@Validated
public class AdminGroupController {

    private final GroupService groupService;

    public AdminGroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public Page<GroupDtoForAdminPage> get(
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size
    ) {
        return groupService.getAllForAdmin(page, size);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable @Positive Long id,
            @RequestBody GroupDtoForAdminUpdate groupDtoForAdminUpdate
    ) {
        groupService.update(id, groupDtoForAdminUpdate);
    }
}
