package ru.edjll.backend.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
