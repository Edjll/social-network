package ru.edjll.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDtoForChangeEnabled {

    @NotNull
    private boolean enabled;
}
