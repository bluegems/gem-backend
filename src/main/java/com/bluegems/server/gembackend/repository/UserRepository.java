package com.bluegems.server.gembackend.repository;

import com.bluegems.server.gembackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "FROM users u WHERE u.username=:username AND u.tag=:tag")
    Optional<UserEntity> fetchUserByUsernameAndTag(@Param("username") String username, @Param("tag") String tag);

    @Query(value = "FROM users u WHERE u.accountEntity.email=:email")
    Optional<UserEntity> fetchUserByEmail(@Param("email") String email);

    @Query(value = "SELECT u FROM users u WHERE u.accountEntity.email IN ('blossom@bluegems.com','buttercup@bluegems.com','bubbles@bluegems.com')")
    List<UserEntity> fetchPowerpuffGirls();

    @Query(value = "SELECT CASE WHEN (COUNT(u)>0) THEN true ELSE false END " +
            "FROM users u " +
            "WHERE u.accountEntity.email=:currentUserEmail AND u.username=:requestedUserUsername AND u.tag=:requestedUserTag")
    Boolean isCurrentUser(
            @Param("currentUserEmail") String currentUserEmail,
            @Param("requestedUserUsername") String requestedUserUsername,
            @Param("requestedUserTag") String requestedUserTag
    );
}
