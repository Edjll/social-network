package ru.edjll.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDtoForUpdate {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
