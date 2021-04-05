package ru.edjll.backend.dto.userInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserInfoDto {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDate birthday;
    private String city;

    public UserInfoDto(String id, String firstName, String lastName, String username, LocalDate birthday, String city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.birthday = birthday;
        this.city = city;
    }
}
