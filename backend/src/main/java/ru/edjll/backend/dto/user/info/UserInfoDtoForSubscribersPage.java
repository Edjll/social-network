package ru.edjll.backend.dto.user.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDtoForSubscribersPage {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String city;
    private int status = 1;

    public UserInfoDtoForSubscribersPage(String id, String username, String firstName, String lastName, String city) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
    }
}
