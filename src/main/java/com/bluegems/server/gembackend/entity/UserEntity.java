package com.bluegems.server.gembackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "username_tag_unique", columnNames = {"tag", "username"})
        }
)
public class UserEntity extends AuditEntity implements Comparable<UserEntity> {

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

    @Column(name = "profile_picture")
    private String profilePicture;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private AccountEntity accountEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id.equals(that.id) && tag.equals(that.tag) && username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tag, username);
    }

    @Override
    public int compareTo(@NotNull UserEntity other) {
        return (int) (this.id-other.getId());
    }

    @Override
    public String toString() {
        return "{"+username+"#"+tag+"}";
    }

    public Boolean isGreaterThan(@NotNull UserEntity that) {
        return this.compareTo(that) > 0;
    }

    public Boolean isLessThan(@NotNull UserEntity that) {
        return this.compareTo(that) < 0;
    }
}
