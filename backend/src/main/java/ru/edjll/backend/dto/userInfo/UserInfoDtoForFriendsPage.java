package ru.edjll.backend.dto.userInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDtoForFriendsPage {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String city;
}
