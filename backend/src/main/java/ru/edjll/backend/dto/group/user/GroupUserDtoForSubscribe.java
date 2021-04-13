package ru.edjll.backend.dto.group.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserDtoForSubscribe {

    @NotNull
    @Positive
    @Exists(table = "groups", column = "id")
    private Long groupId;
}
