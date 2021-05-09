package ru.edjll.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.dto.user.info.UserInfoDtoForFriendsPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;
import ru.edjll.backend.dto.user.info.UserInfoDtoForUserCart;

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
        }
)

@SqlResultSetMapping(
        name = "userInfoDtoForFriendsPageResultMapping",
        classes = @ConstructorResult(
                targetClass = UserInfoDtoForFriendsPage.class,
                columns = {
                        @ColumnResult(name="id",            type = String.class),
                        @ColumnResult(name="username",      type = String.class),
                        @ColumnResult(name="first_name",    type = String.class),
                        @ColumnResult(name="last_name",     type = String.class),
                        @ColumnResult(name="city",          type = String.class)
                }
        )
)

@SqlResultSetMapping(
        name = "userInfoDtoForUserCartResultMapping",
        classes = @ConstructorResult(
                targetClass = UserInfoDtoForUserCart.class,
                columns = {
                        @ColumnResult(name="username",      type = String.class),
                        @ColumnResult(name="first_name",    type = String.class)
                }
        )
)

@NamedNativeQuery(
        name = "UserFriend.getAllFriendsDto",
        query = "select user_entity.id, user_entity.username, user_entity.first_name, user_entity.last_name, city.title as city " +
                "from   ( " +
                "        select friend_id as id " +
                "        from user_friend " +
                "        where user_friend.user_id = :user_id and status = 0 " +
                "        union " +
                "        select user_id as id " +
                "        from user_friend " +
                "        where user_friend.friend_id = :user_id and status = 0 " +
                "    ) as user_id " +
                "    join user_entity on user_entity.id = user_id.id " +
                "    left join user_info on user_entity.id = user_info.user_id " +
                "    left join city on user_info.city_id = city.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and case when :first_name = '' then true else lower(:first_name) = lower(user_entity.first_name) end " +
                "  and case when :last_name = '' then true else lower(:last_name) = lower(user_entity.last_name) end " +
                "  and case when :city_id = -1 then true else :city_id = city.id end " +
                "  and case when :city_id <> -1 or :country_id = -1 then true else :country_id = city.country_id end",
        resultSetMapping = "userInfoDtoForFriendsPageResultMapping"
)

@NamedNativeQuery(
        name = "UserFriend.getAllFriendCardsDto",
        query = "select user_entity.username, user_entity.first_name " +
                "from   ( " +
                "        select friend_id as id " +
                "        from user_friend " +
                "        where user_friend.user_id = :user_id and status = 0 " +
                "        union " +
                "        select user_id as id " +
                "        from user_friend " +
                "        where user_friend.friend_id = :user_id and status = 0 " +
                "    ) as user_id " +
                "    join user_entity on user_entity.id = user_id.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true",
        resultSetMapping = "userInfoDtoForUserCartResultMapping"
)

@NamedNativeQuery(
        name = "UserFriend.getAllSubscribersDto",
        query = "select user_entity.id, user_entity.username, user_entity.first_name, user_entity.last_name, city.title as city " +
                "from user_friend " +
                "     join user_entity on user_entity.id = user_friend.friend_id " +
                "     left join user_info on user_entity.id = user_info.user_id " +
                "     left join city on user_info.city_id = city.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and user_friend.user_id= :user_id " +
                "  and user_friend.status = 1 " +
                "  and case when :first_name = '' then true else lower(:first_name) = lower(user_entity.first_name) end " +
                "  and case when :last_name = '' then true else lower(:last_name) = lower(user_entity.last_name) end " +
                "  and case when :city_id = -1 then true else :city_id = city.id end " +
                "  and case when :city_id <> -1 or :country_id = -1 then true else :country_id = city.country_id end",
        resultSetMapping = "userInfoDtoForFriendsPageResultMapping"
)

@NamedNativeQuery(
        name = "UserFriend.getAllSubscriberCardsDto",
        query = "select user_entity.username, user_entity.first_name " +
                "from user_friend " +
                "     join user_entity on user_entity.id = user_friend.friend_id " +
                " where realm_id = 'social-network'  " +
                "   and service_account_client_link is null " +
                "   and user_entity.enabled = true " +
                "   and user_friend.user_id= :user_id " +
                "   and user_friend.status = 1",
        resultSetMapping = "userInfoDtoForUserCartResultMapping"
)

public class UserFriend {

    @EmbeddedId
    private UserFriendKey id;

    private LocalDateTime date;

    @Enumerated(EnumType.ORDINAL)
    private UserFriendStatus status;
}
