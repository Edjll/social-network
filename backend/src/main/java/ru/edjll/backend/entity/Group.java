package ru.edjll.backend.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ru.edjll.backend.dto.group.GroupDtoForAdminPage;
import ru.edjll.backend.dto.user.InterlocutorDto;
import ru.edjll.backend.dto.user.UserDtoForAdminPage;
import ru.edjll.backend.dto.user.info.UserInfoDtoForSearch;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"users", "posts"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "groups",
    indexes = {
        @Index(name = "groups_creator_id", columnList = "creator_id"),
        @Index(name = "groups_address", columnList = "address")
    }
)

@SqlResultSetMapping(
        name = "groupDtoForAdminPageResultMapping",
        classes = @ConstructorResult(
                targetClass = GroupDtoForAdminPage.class,
                columns = {
                        @ColumnResult(name="id",            type = Long.class),
                        @ColumnResult(name="title",         type = String.class),
                        @ColumnResult(name="description",   type = String.class),
                        @ColumnResult(name="address",       type = String.class),
                        @ColumnResult(name="enabled",       type = Boolean.class)
                }
        )
)
@NamedNativeQuery(
        name = "Group.getAllForAdmin",
        query = "select groups.id, groups.title, groups.description, groups.address, groups.enabled " +
                "from groups " +
                "where " +
                "  case when :id = -1 then true else :id = groups.id end " +
                "  and case when :title = '' then true else lower(:title) = lower(groups.title) end " +
                "  and case when :address = '' then true else lower(:address) = lower(groups.address) end " +
                "order by " +
                "   case when :id_direction = 'asc' then groups.id end asc, " +
                "   case when :id_direction = 'desc' then groups.id end desc, " +
                "   case when :title_direction = 'asc' then groups.title end asc, " +
                "   case when :title_direction = 'desc' then groups.title end desc, " +
                "   case when :address_direction = 'asc' then groups.address end asc, " +
                "   case when :address_direction = 'desc' then groups.address end desc, " +
                "   case when :enabled_direction = 'asc' then groups.enabled end asc, " +
                "   case when :enabled_direction = 'desc' then groups.enabled end desc",
        resultSetMapping = "groupDtoForAdminPageResultMapping"
)
@NamedNativeQuery(
        name = "Group.getCountForAdmin",
        query = "select count(groups.id) as count " +
                "from groups " +
                "where " +
                "  case when :id = -1 then true else :id = groups.id end " +
                "  and case when :title = '' then true else lower(:title) = lower(groups.title) end " +
                "  and case when :address = '' then true else lower(:address) = lower(groups.address) end",
        resultSetMapping = "countResultMapping"
)

public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "address", unique = true)
    private String address;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private Set<GroupUser> users;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id")
    private Set<GroupPost> posts;
}
