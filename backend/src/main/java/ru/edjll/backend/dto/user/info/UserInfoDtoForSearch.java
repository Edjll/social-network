package ru.edjll.backend.dto.user.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.UserFriendStatus;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDtoForSearch {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String city;
    private UserFriendStatus status;
    private String friendId;

    public UserInfoDtoForSearch(String id, String username, String firstName, String lastName, String city, Integer status, String friendId) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        if (status == null) {
            this.status = null;
        } else {
            this.status = Arrays.stream(UserFriendStatus.values()).filter(s -> s.ordinal() == status).findFirst().get();
        }
        this.friendId = friendId;
    }
}
