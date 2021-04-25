package com.bluegems.server.gembackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.annotation.processing.Generated;
import javax.persistence.*;

@Entity(name = "post")
@Table(name = "post")
public class PostEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private UserEntity userEntity;
}
