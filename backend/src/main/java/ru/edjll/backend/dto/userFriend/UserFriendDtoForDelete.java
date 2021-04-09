package ru.edjll.backend.dto.userFriend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.User;
import ru.edjll.backend.entity.UserFriend;
import ru.edjll.backend.entity.UserFriendKey;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendDtoForDelete {

    @NotEmpty
    private String userId;

    @NotEmpty
    private String friendId;

    public UserFriendKey toUserFriendKey() {
        User user = new User();
        User friend = new User();

        user.setId(userId);
        friend.setId(friendId);

        return new UserFriendKey(user, friend);
    }
}
