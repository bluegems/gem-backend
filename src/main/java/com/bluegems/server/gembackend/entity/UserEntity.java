package com.bluegems.server.gembackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "users")
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "username_tag_unique", columnNames = {"tag", "username"})
        }
)
public class UserEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tag", nullable = false)
    private String tag;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "bio")
    private String bio;

    @Column(name = "birthdate")
    private Date birthdate;

    @Column(name = "profile_picture")
    private String profilePicture;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private AccountEntity accountEntity;
}