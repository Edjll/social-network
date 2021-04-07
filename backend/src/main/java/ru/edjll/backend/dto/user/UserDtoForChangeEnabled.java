package ru.edjll.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.repository.UserRepository;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoForChangeEnabled {

    @NotEmpty(message = "{user.id.notEmpty}")
    @Exists(table = "user_entity", column = "id", message = "{user.id.exists}")
    private String id;

    private boolean enabled;
}
