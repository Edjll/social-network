package ru.edjll.backend.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Group;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDtoForUpdate {

    @NotNull
    @Positive
    @Exists(table = "groups", column = "id")
    private Long id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private String address;

    public Group toGroup() {
        Group group = new Group();

        group.setId(id);
        group.setTitle(title);
        group.setDescription(description);
        group.setAddress(address);

        return group;
    }
}
