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
@Table(name = "user_friend")
public class UserFriend {

    @EmbeddedId
    private UserFriendKey id;

    private LocalDateTime date;

    @Enumerated(EnumType.ORDINAL)
    private UserFriendStatus status;
}
