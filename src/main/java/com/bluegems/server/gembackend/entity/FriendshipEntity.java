package com.bluegems.server.gembackend.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "friendship")
@Table(
        name = "friendship",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_one_two_unique", columnNames = {"user_one", "user_two"})
        }
)
@IdClass(FriendshipId.class)
public class FriendshipEntity extends AuditEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_one", nullable = false)
    private UserEntity userOne;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_two", nullable = false)
    private UserEntity userTwo;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "modified_by", nullable = false)
    private UserEntity modifiedBy;
}
