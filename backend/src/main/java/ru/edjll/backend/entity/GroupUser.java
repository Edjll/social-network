package ru.edjll.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.dto.group.user.GroupUserDtoForGroupPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "group_user")

// getAllByAnonymousUser
@NamedNativeQuery(
        name = "GroupUser.getAllDtoByAnonymousUser",
        query = "select user_entity.id, user_entity.username, user_entity.first_name, user_entity.last_name, city.title as city, null as status, null as friend_id " +
                "from group_user " +
                "    join user_entity on user_entity.id = group_user.user_id " +
                "    left join user_info on user_entity.id = user_info.user_id " +
                "    left join city on user_info.city_id = city.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and group_user.group_id = :group_id " +
                "  and case when :first_name = '' then true else lower(:first_name) = lower(user_entity.first_name) end " +
                "  and case when :last_name = '' then true else lower(:last_name) = lower(user_entity.last_name) end " +
                "  and case when :city_id = -1 then true else :city_id = city.id end " +
                "  and case when :city_id <> -1 or :country_id = -1 then true else :country_id = city.country_id end",
        resultSetMapping = "userInfoDtoForSearchResultMapping"
)
@NamedNativeQuery(
        name = "GroupUser.getAllDtoByAnonymousUser.count",
        query = "select count(user_entity.id) as count " +
                "from group_user " +
                "    join user_entity on user_entity.id = group_user.user_id " +
                "    left join user_info on user_entity.id = user_info.user_id " +
                "    left join city on user_info.city_id = city.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and group_user.group_id = :group_id " +
                "  and case when :first_name = '' then true else lower(:first_name) = lower(user_entity.first_name) end " +
                "  and case when :last_name = '' then true else lower(:last_name) = lower(user_entity.last_name) end " +
                "  and case when :city_id = -1 then true else :city_id = city.id end " +
                "  and case when :city_id <> -1 or :country_id = -1 then true else :country_id = city.country_id end",
        resultSetMapping = "countResultMapping"
)

// getAllByAuthorizedUser
@NamedNativeQuery(
        name = "GroupUser.getAllDtoByAuthorizedUser",
        query = "select user_entity.id, user_entity.username, user_entity.first_name, user_entity.last_name, city.title as city, status, friend_id " +
                "from group_user " +
                "    join user_entity on user_entity.id = group_user.user_id " +
                "    left join user_info on user_entity.id = user_info.user_id " +
                "    left join city on user_info.city_id = city.id " +
                "    left join ( " +
                "        select friend_id as id, status, friend_id " +
                "        from user_friend " +
                "        where user_friend.user_id = :user_id " +
                "        union " +
                "        select user_id as id, status, friend_id " +
                "        from user_friend " +
                "        where user_friend.friend_id = :user_id " +
                "    ) as user_friend on user_entity.id = user_friend.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and group_user.group_id = :group_id " +
                "  and case when :first_name = '' then true else lower(:first_name) = lower(user_entity.first_name) end " +
                "  and case when :last_name = '' then true else lower(:last_name) = lower(user_entity.last_name) end " +
                "  and case when :city_id = -1 then true else :city_id = city.id end " +
                "  and case when :city_id <> -1 or :country_id = -1 then true else :country_id = city.country_id end",
        resultSetMapping = "userInfoDtoForSearchResultMapping"
)
@NamedNativeQuery(
        name = "GroupUser.getAllDtoByAuthorizedUser.count",
        query = "select count(user_entity.id) as count " +
                "from group_user " +
                "    join user_entity on user_entity.id = group_user.user_id " +
                "    left join user_info on user_entity.id = user_info.user_id " +
                "    left join city on user_info.city_id = city.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and group_user.group_id = :group_id " +
                "  and case when :first_name = '' then true else lower(:first_name) = lower(user_entity.first_name) end " +
                "  and case when :last_name = '' then true else lower(:last_name) = lower(user_entity.last_name) end " +
                "  and case when :city_id = -1 then true else :city_id = city.id end " +
                "  and case when :city_id <> -1 or :country_id = -1 then true else :country_id = city.country_id end",
        resultSetMapping = "countResultMapping"
)

// groupUserDtoForGroupPageResultMapping
@SqlResultSetMapping(
        name = "groupUserDtoForGroupPageResultMapping",
        classes = @ConstructorResult(
                targetClass = GroupUserDtoForGroupPage.class,
                columns = {
                        @ColumnResult(name="username",      type = String.class),
                        @ColumnResult(name="first_name",    type = String.class)
                }
        )
)

// getCardDtoByAuthorizedUser
@NamedNativeQuery(
        name = "GroupUser.getCardDtoByAuthorizedUser",
        query = "select username, first_name " +
                "from group_user " +
                "    join user_entity on group_user.user_id = user_entity.id and group_user.group_id = :group_id " +
                "where user_id = :user_id " +
                "union " +
                "(" +
                "    select username, first_name " +
                "    from group_user " +
                "             join user_entity on group_user.user_id = user_entity.id and group_user.group_id = :group_id " +
                "    limit :size " +
                ") " +
                "limit :size",
        resultSetMapping = "groupUserDtoForGroupPageResultMapping"
)

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
