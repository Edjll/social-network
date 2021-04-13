package ru.edjll.backend.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.Group;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDtoForSave {

    @NotEmpty
    private String title;

    @NotEmpty
    private String description;

    @NotEmpty
    private String address;

    public Group toGroup() {
        Group group = new Group();

        group.setTitle(title);
        group.setDescription(description);
        group.setCreatedDate(LocalDateTime.now());
        group.setEnabled(true);
        group.setAddress(address);

        return group;
    }
}
