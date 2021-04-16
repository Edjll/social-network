package ru.edjll.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_entity")
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
