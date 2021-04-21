package ru.edjll.backend.dto.group;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDtoForAdminUpdate {

    @NotNull
    private Boolean enabled;
}
