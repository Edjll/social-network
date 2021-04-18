package ru.edjll.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.dto.group.user.GroupUserDtoForGroupPage;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_user")
public class GroupUser {

    @EmbeddedId
    private GroupUserKey id;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    public GroupUser(Group group, User user, LocalDateTime createdDate) {
        this.id = new GroupUserKey(group, user);
        this.createdDate = createdDate;
    }
}