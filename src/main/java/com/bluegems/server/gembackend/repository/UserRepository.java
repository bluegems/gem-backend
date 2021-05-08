package com.bluegems.server.gembackend.repository;

import com.bluegems.server.gembackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "FROM users u WHERE u.username=:username AND u.tag=:tag", nativeQuery = false)
    Optional<UserEntity> fetchUserByUsernameAndTag(@Param("username") String username, @Param("tag") String tag);

    @Query(value = "FROM users u WHERE u.accountEntity.email=:email", nativeQuery = false)
    Optional<UserEntity> fetchUserByEmail(@Param("email") String email);
}
