package ru.edjll.backend.dto.user.friend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.entity.UserFriendStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFriendStatusDto {

    private UserFriendStatus status;
    private String friendId;
}
