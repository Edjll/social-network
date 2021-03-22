package ru.edjll.backend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
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
