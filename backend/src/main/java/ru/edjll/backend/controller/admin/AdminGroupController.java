package ru.edjll.backend.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.edjll.backend.dto.group.GroupDtoForAdminPage;
import ru.edjll.backend.dto.group.GroupDtoForAdminUpdate;
import ru.edjll.backend.service.GroupService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/groups")
@Validated
public class AdminGroupController {

    private final GroupService groupService;

    public AdminGroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<GroupDtoForAdminPage> getAll(
            @RequestParam @NotNull @PositiveOrZero Integer page,
            @RequestParam @NotNull @Positive Integer size,
            @RequestParam(defaultValue = "") String idDirection,
            @RequestParam(defaultValue = "") String titleDirection,
            @RequestParam(defaultValue = "") String addressDirection,
            @RequestParam(defaultValue = "") String enabledDirection,
            @RequestParam(defaultValue = "-1") Long id,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String address
    ) {
        return groupService.getAllForAdmin(page, size, idDirection, titleDirection, addressDirection, enabledDirection, id, title, address);
    }

    @GetMapping("/count")
    public Integer getCount(
            @RequestParam(defaultValue = "-1") Long id,
            @RequestParam(defaultValue = "") String title,
            @RequestParam(defaultValue = "") String address
    ) {
        return groupService.getCountForAdmin(id, title, address);
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
