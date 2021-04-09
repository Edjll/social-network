package ru.edjll.backend.dto.userFriend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.entity.UserFriend;
import ru.edjll.backend.entity.UserFriendKey;
import ru.edjll.backend.entity.UserFriendStatus;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendDtoForUpdate {

    @NotEmpty
    @Exists(table = "user_entity", column = "id")
    private String userId;

    @NotEmpty
    @Exists(table = "user_entity", column = "id")
    private String friendId;

    @NotNull
    private UserFriendStatus status;

    @NotNull
    private LocalDateTime date;

    public UserFriend toUserFriend() {
        UserFriend userFriend = new UserFriend();
        User user = new User();
        User friend = new User();

        user.setId(userId);
        friend.setId(friendId);

        userFriend.setId(new UserFriendKey(user, friend));
        userFriend.setStatus(status);
        userFriend.setDate(date);

        return userFriend;
    }
}
