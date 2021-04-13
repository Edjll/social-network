package ru.edjll.backend.dto.group.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserDtoForGroupPage {

    private String username;
    private String firstName;
}
