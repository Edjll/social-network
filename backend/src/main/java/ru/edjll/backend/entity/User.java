package ru.edjll.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.edjll.backend.dto.post.PostDto;
import ru.edjll.backend.dto.user.InterlocutorDto;
import ru.edjll.backend.dto.user.UserDtoForAdminPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "user_entity",
        indexes = {
                @Index(name = "user_entity_index__id__enabled", columnList = "id, enabled")
        }
)
// userInfoDtoForSearchResultMapping
@SqlResultSetMapping(
        name = "userInfoDtoForSearchResultMapping",
        classes = @ConstructorResult(
                targetClass = UserInfoDtoForSearch.class,
                columns = {
                        @ColumnResult(name="id",            type = String.class),
                        @ColumnResult(name="username",      type = String.class),
                        @ColumnResult(name="first_name",    type = String.class),
                        @ColumnResult(name="last_name",     type = String.class),
                        @ColumnResult(name="city",          type = String.class),
                        @ColumnResult(name="status",        type = Integer.class),
                        @ColumnResult(name="friend_id",     type = String.class)
                }
        )
)
// countResultMapping
@SqlResultSetMapping(name = "countResultMapping", columns = @ColumnResult(name = "count"))
// searchUsersByAuthorizedUser
@NamedNativeQuery(
        name = "User.searchUsersByAuthorizedUser",
        query = "select " +
                "       user_entity.id, user_entity.username, user_entity.first_name, " +
                "       user_entity.last_name, city.title as city, user_friend.status, user_friend.friend_id " +
                "from user_entity " +
                "    left join user_info on user_entity.id = user_info.user_id " +
                "    left join city on user_info.city_id = city.id " +
                "    left join ( " +
                "        select friend_id as id, status, friend_id " +
                "        from user_friend " +
                "        where user_friend.user_id = :user_id " +
                "        union " +
                "        select user_id as id, status, friend_id " +
                "        from user_friend " +
                "        where user_friend.friend_id = :user_id) as user_friend " +
                "    on user_entity.id = user_friend.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and user_entity.id != 'b65bfe43-77dd-44f1-8199-a9dfa3946da7' " +
                "  and case when :first_name = '' then true else lower(:first_name) = lower(user_entity.first_name) end " +
                "  and case when :last_name = '' then true else lower(:last_name) = lower(user_entity.last_name) end " +
                "  and case when :city_id = -1 then true else :city_id = city.id end " +
                "  and case when :city_id <> -1 or :country_id = -1 then true else :country_id = city.country_id end",
        resultSetMapping = "userInfoDtoForSearchResultMapping"
)
@NamedNativeQuery(
        name = "User.searchUsersByAuthorizedUser.count",
        query = "select count(user_entity.id) as count, :user_id " +
                "from user_entity " +
                "    left join user_info on user_entity.id = user_info.user_id " +
                "    left join city on user_info.city_id = city.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and user_entity.id != 'b65bfe43-77dd-44f1-8199-a9dfa3946da7' " +
                "  and case when :first_name = '' then true else lower(:first_name) = lower(user_entity.first_name) end " +
                "  and case when :last_name = '' then true else lower(:last_name) = lower(user_entity.last_name) end " +
                "  and case when :city_id = -1 then true else :city_id = city.id end " +
                "  and case when :city_id <> -1 or :country_id = -1 then true else :country_id = city.country_id end",
        resultSetMapping = "countResultMapping"
)
// searchUsersByAnonymousUser
@NamedNativeQuery(
        name = "User.searchUsersByAnonymousUser",
        query = "select " +
                "       user_entity.id, user_entity.username, user_entity.first_name, " +
                "       user_entity.last_name, city.title as city, null as status, null as friend_id " +
                "from user_entity " +
                "    left join user_info on user_entity.id = user_info.user_id " +
                "    left join city on user_info.city_id = city.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and user_entity.id != 'b65bfe43-77dd-44f1-8199-a9dfa3946da7' " +
                "  and case when :first_name = '' then true else lower(:first_name) = lower(user_entity.first_name) end " +
                "  and case when :last_name = '' then true else lower(:last_name) = lower(user_entity.last_name) end " +
                "  and case when :city_id = -1 then true else :city_id = city.id end " +
                "  and case when :city_id <> -1 or :country_id = -1 then true else :country_id = city.country_id end",
        resultSetMapping = "userInfoDtoForSearchResultMapping"
)
@NamedNativeQuery(
        name = "User.searchUsersByAnonymousUser.count",
        query = "select count(user_entity.id) as count " +
                "from user_entity " +
                "    left join user_info on user_entity.id = user_info.user_id " +
                "    left join city on user_info.city_id = city.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and user_entity.id != 'b65bfe43-77dd-44f1-8199-a9dfa3946da7' " +
                "  and case when :first_name = '' then true else lower(:first_name) = lower(user_entity.first_name) end " +
                "  and case when :last_name = '' then true else lower(:last_name) = lower(user_entity.last_name) end " +
                "  and case when :city_id = -1 then true else :city_id = city.id end " +
                "  and case when :city_id <> -1 or :country_id = -1 then true else :country_id = city.country_id end",
        resultSetMapping = "countResultMapping"
)

// userDtoForAdminPageResultMapping
@SqlResultSetMapping(
        name = "userDtoForAdminPageResultMapping",
        classes = @ConstructorResult(
                targetClass = UserDtoForAdminPage.class,
                columns = {
                        @ColumnResult(name="id",       type = String.class),
                        @ColumnResult(name="username", type = String.class),
                        @ColumnResult(name="email",    type = String.class),
                        @ColumnResult(name="city",     type = String.class),
                        @ColumnResult(name="enabled",  type = Boolean.class)
                }
        )
)
// getUsersForAdmin
@NamedNativeQuery(
        name = "User.getAllForAdmin",
        query = "select user_entity.id, user_entity.username, user_entity.email, city.title as city, user_entity.enabled " +
                "from user_entity " +
                "    left join user_info on user_entity.id = user_info.user_id " +
                "    left join city on user_info.city_id = city.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and user_entity.id != 'b65bfe43-77dd-44f1-8199-a9dfa3946da7' " +
                "  and case when :id = '' then true else :id = user_entity.username end " +
                "  and case when :username = '' then true else lower(:username) = lower(user_entity.username) end " +
                "  and case when :email = '' then true else lower(:email) = lower(user_entity.email) end " +
                "  and case when :city = '' then true else lower(:city) = lower(city.title) end " +
                "order by " +
                "   case when :id_direction = 'asc' then user_entity.id end asc, " +
                "   case when :id_direction = 'desc' then user_entity.id end desc, " +
                "   case when :username_direction = 'asc' then user_entity.username end asc, " +
                "   case when :username_direction = 'desc' then user_entity.username end desc, " +
                "   case when :email_direction = 'asc' then user_entity.email end asc, " +
                "   case when :email_direction = 'desc' then user_entity.email end desc, " +
                "   case when :city_direction = 'asc' then city.title end asc, " +
                "   case when :city_direction = 'desc' then city.title end desc, " +
                "   case when :enabled_direction = 'asc' then user_entity.enabled end asc, " +
                "   case when :enabled_direction = 'desc' then user_entity.enabled end desc",
        resultSetMapping = "userDtoForAdminPageResultMapping"
)
@NamedNativeQuery(
        name = "User.getCountForAdmin",
        query = "select count(user_entity.id) as count " +
                "from user_entity " +
                "    left join user_info on user_entity.id = user_info.user_id " +
                "    left join city on user_info.city_id = city.id " +
                "where realm_id = 'social-network' " +
                "  and service_account_client_link is null " +
                "  and user_entity.enabled = true " +
                "  and user_entity.id != 'b65bfe43-77dd-44f1-8199-a9dfa3946da7' " +
                "  and case when :id = '' then true else :id = user_entity.username end " +
                "  and case when :username = '' then true else lower(:username) = lower(user_entity.username) end " +
                "  and case when :email = '' then true else lower(:email) = lower(user_entity.email) end " +
                "  and case when :city = '' then true else lower(:city) = lower(city.title) end",
        resultSetMapping = "countResultMapping"
)

// postDtoResultMapping
@SqlResultSetMapping(
        name = "postDtoResultMapping",
        classes = @ConstructorResult(
                targetClass = PostDto.class,
                columns = {
                        @ColumnResult(name="id",            type = Long.class),
                        @ColumnResult(name="text",          type = String.class),
                        @ColumnResult(name="created_date",  type = LocalDateTime.class),
                        @ColumnResult(name="modified_date", type = LocalDateTime.class),
                        @ColumnResult(name="creator_id",    type = String.class),
                        @ColumnResult(name="name",          type = String.class),
                        @ColumnResult(name="address",       type = String.class),
                        @ColumnResult(name="type",          type = String.class)
                }
        )
)

// getFeed
@NamedNativeQuery(
        name = "User.getFeed",
        query = "select post.id, post.text, post.created_date, post.modified_date, post.user_id as creator_id, " +
                "       concat(user_entity.first_name, ' ', user_entity.last_name) as name, " +
                "       user_entity.username as address, 'USER' as type " +
                "from post " +
                "    join ( " +
                "        select user_friend.friend_id as id " +
                "        from user_friend " +
                "        where user_friend.user_id = :id and user_friend.status = 0 " +
                "        union " +
                "        select user_friend.user_id as id " +
                "        from user_friend " +
                "        where user_friend.friend_id = :id) as users " +
                "        on post.user_id = users.id " +
                "    join user_entity on user_entity.id = post.user_id " +
                "where user_entity.enabled = true " +
                "union all " +
                "select group_post.id, group_post.text, group_post.created_date, group_post.modified_date, " +
                "       groups.creator_id as creator_id, groups.title as name, " +
                "       groups.address as address, 'GROUP' as type " +
                "from group_post " +
                "    join groups on group_post.group_id = groups.id " +
                "    join group_user on group_user.user_id = :id " +
                "        and groups.id = group_user.group_id " +
                "where groups.enabled = true " +
                "order by created_date desc ",
        resultSetMapping = "postDtoResultMapping"
)
@NamedNativeQuery(
        name = "User.getFeed.count",
        query = "select sum(value) as count " +
                "from ( " +
                "    select count(*) as value " +
                "    from post " +
                "        join " +
                "            (select user_friend.friend_id as id " +
                "             from user_friend " +
                "             where user_friend.user_id = :id " +
                "             union " +
                "             select user_friend.user_id as id " +
                "             from user_friend " +
                "             where user_friend.friend_id = :id " +
                "        ) as users on post.user_id = users.id " +
                "    union all " +
                "    select count(*) as value " +
                "    from group_post " +
                "    join group_user on group_user.user_id = :id " +
                "        and group_user.group_id = group_post.group_id " +
                "    ) as size",
        resultSetMapping = "countResultMapping"
)

// interlocutorDtoResultMapping
@SqlResultSetMapping(
        name = "interlocutorDtoResultMapping",
        classes = @ConstructorResult(
                targetClass = InterlocutorDto.class,
                columns = {
                        @ColumnResult(name="id",            type = String.class),
                        @ColumnResult(name="username",      type = String.class),
                        @ColumnResult(name="first_name",    type = String.class),
                        @ColumnResult(name="last_name",     type = String.class),
                        @ColumnResult(name="position",      type = Integer.class),
                        @ColumnResult(name="new_messages",  type = Integer.class)
                }
        )
)

// getInterlocutors
@NamedNativeQuery(
        name = "User.getInterlocutors",
        query = "select user_entity.id, user_entity.username, user_entity.first_name, user_entity.last_name, new_messages, null as position " +
                "from user_entity  " +
                "    join (  " +
                "        select user_id, max(date) as date, sum(count) as new_messages " +
                "        from (  " +
                "             select  " +
                "                sender_id as user_id, " +
                "                max(created_date) as date,  " +
                "                count(case when viewed = false then 1 end) as count " +
                "             from message  " +
                "             where recipient_id = :id  " +
                "             group by user_id  " +
                "             union all  " +
                "             select  " +
                "                recipient_id as user_id, " +
                "                max(created_date) as date, " +
                "                '0'  " +
                "             from message " +
                "             where sender_id = :id " +
                "             group by user_id " +
                "        ) as info  " +
                "        group by user_id " +
                "    ) as info  " +
                "    on user_entity.id = info.user_id " +
                "order by info.date desc",
        resultSetMapping = "interlocutorDtoResultMapping"
)
@NamedNativeQuery(
        name = "User.getInterlocutors.count",
        query = "select count(*) as count " +
                "from ( " +
                "    select sender_id as user_id " +
                "    from message  " +
                "    where recipient_id = :id" +
                "    union  " +
                "    select recipient_id as user_id " +
                "    from message " +
                "    where sender_id = :id " +
                ") as info",
        resultSetMapping = "countResultMapping"
)

// getInterlocutor
@NamedNativeQuery(
        name = "User.getInterlocutor",
        query = "select user_entity.id, user_entity.username, user_entity.first_name, user_entity.last_name, position, new_messages " +
                "from user_entity join ( " +
                "        select user_id, row_number() over (order by max(date) desc) as position, sum(count) as new_messages " +
                "        from ( " +
                "                 select " +
                "                         sender_id as user_id, " +
                "                         max(created_date) as date, " +
                "                         count(case when viewed = false then 1 end) as count " +
                "                 from message " +
                "                 where recipient_id = :id " +
                "                 group by user_id " +
                "                 union all " +
                "                 select " +
                "                         recipient_id as user_id, " +
                "                         max(created_date) as date, " +
                "                        '0' as count " +
                "                 from message  " +
                "                 where sender_id = :id " +
                "                 group by user_id  " +
                "         ) as info " +
                "        group by user_id) as info " +
                "on user_entity.id = :interlocutor_id and user_entity.id = user_id",
        resultSetMapping = "interlocutorDtoResultMapping"
)

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(name = "email")
    private String email;

    @Column(name = "email_constraint")
    private String emailConstraint;

    @Column(name = "email_verified")
    private boolean emailVerified;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "federation_link")
    private String federationLink;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "realm_id")
    private String realmId;

    @Column(name = "not_before")
    private int notBefore;

    @Column(name = "username")
    private String username;

    @Column(name = "created_timestamp")
    private long createdTimestamp;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private UserInfo userInfo;

    @Column(name = "service_account_client_link", length = 36)
    private String serviceAccountClientLink;
}
