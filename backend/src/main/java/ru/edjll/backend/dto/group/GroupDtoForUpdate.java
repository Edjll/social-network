package ru.edjll.backend.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.edjll.backend.entity.Group;

import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDtoForUpdate {

    @Length(min = 5, max = 40)
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9_\\- ]+$")
    private String title;

    @Length(min = 10, max = 150)
    private String description;

    @Length(min = 3, max = 15)
    @Pattern(regexp = "^[a-zA-Z0-9_]+$")
    private String address;

    public Group toGroup() {
        Group group = new Group();

        group.setTitle(title);
        group.setDescription(description);

        return group;
    }
}
