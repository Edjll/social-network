package ru.edjll.backend.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDtoForDelete {

    @NotNull
    @Positive
    @Exists(table = "groups", column = "id")
    private Long id;

    @NotEmpty
    @Exists(table = "user_entity", column = "id")
    private String creatorId;
}
