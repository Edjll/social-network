package ru.edjll.backend.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_entity")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String email;

    private String emailConstraint;

    private boolean emailVerified;

    private boolean enabled;

    private String federationLink;

    private String firstName;

    private String lastName;

    private String realmId;

    private int notBefore;

    private String username;

    private long createdTimestamp;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private UserInfo userInfo;

    @Column(length = 36)
    private String serviceAccountClientLink;
}
