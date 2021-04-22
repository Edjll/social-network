package ru.edjll.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_friend",
        indexes = {
                @Index(name = "user_friend_index__user_id", columnList = "user_id"),
                @Index(name = "user_friend_index__friend_id", columnList = "friend_id")
        })
public class UserFriend {

    @EmbeddedId
    private UserFriendKey id;

    private LocalDateTime date;

    @Enumerated(EnumType.ORDINAL)
    private UserFriendStatus status;
}
