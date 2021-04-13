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
}
