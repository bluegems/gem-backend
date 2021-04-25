package com.bluegems.server.gembackend.entity;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "account")
@Table(name = "account")
public class AccountEntity extends AuditEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;
}
