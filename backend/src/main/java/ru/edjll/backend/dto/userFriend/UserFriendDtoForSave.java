package ru.edjll.backend.dto.userFriend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.UserFriend;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.entity.UserFriendKey;
import ru.edjll.backend.entity.UserFriendStatus;
import ru.edjll.backend.validation.exists.Exists;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendDtoForSave {

    @NotEmpty
    @Exists(table = "user_entity", column = "id")
    private String userId;

    @NotEmpty
    @Exists(table = "user_entity", column = "id")
    private String friendId;

    public UserFriend toUserFriend() {
        UserFriend userFriend = new UserFriend();
        User user = new User();
        User friend = new User();

        user.setId(userId);
        friend.setId(friendId);

        userFriend.setId(new UserFriendKey(user, friend));
        userFriend.setDate(LocalDateTime.now());
        userFriend.setStatus(UserFriendStatus.SUBSCRIBER);

        return userFriend;
    }
}
