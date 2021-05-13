package ru.edjll.backend.dto.user.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.UserFriend;
import ru.edjll.backend.entity.UserFriendStatus;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendDtoForUpdate {

    @NotEmpty
    @Exists(table = "user_entity", column = "id")
    private String userId;

    public UserFriend toUserFriend() {
        UserFriend userFriend = new UserFriend();

        userFriend.setStatus(UserFriendStatus.FRIEND);
        userFriend.setDate(LocalDateTime.now());

        return userFriend;
    }
}
