package com.bluegems.server.gembackend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "account")
@Table(name = "account")
public class AccountEntity extends AuditEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
}
