package ru.edjll.backend.dto.user.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.UserFriend;
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

    @NotNull
    private UserFriendStatus status;

    @NotNull
    private LocalDateTime date;

    public UserFriend toUserFriend() {
        UserFriend userFriend = new UserFriend();

        userFriend.setStatus(status);
        userFriend.setDate(date);

        return userFriend;
    }
}
