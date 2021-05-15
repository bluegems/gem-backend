package com.bluegems.server.gembackend.repository;

import com.bluegems.server.gembackend.entity.FriendshipEntity;
import com.bluegems.server.gembackend.entity.FriendshipId;
import com.bluegems.server.gembackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipEntity, FriendshipId> {

    @Query(value = "SELECT CASE WHEN (COUNT(f)>0) THEN true ELSE false END " +
            "FROM friendship f WHERE f.status='ACCEPTED' AND f.userOne=:userOne AND f.userTwo=:userTwo")
    Boolean isFriend(@Param("userOne") UserEntity userOne, @Param("userTwo") UserEntity userTwo);

    @Query(value = "SELECT u FROM users u " +
            "WHERE u in (SELECT f.userOne FROM friendship f WHERE f.status='ACCEPTED' AND f.userTwo=:user) " +
            "OR u in (SELECT f.userTwo FROM friendship f WHERE f.status='ACCEPTED' AND f.userOne=:user)")
    List<UserEntity> fetchFriendsByUser(@Param("user") UserEntity user);

    @Query(value = "FROM friendship f JOIN FETCH f.userOne JOIN FETCH f.userTwo JOIN FETCH f.modifiedBy WHERE f.userOne=:userOne AND f.userTwo=:userTwo")
    Optional<FriendshipEntity> findFriendshipByUsers(@Param("userOne") UserEntity userOne, @Param("userTwo") UserEntity userTwo);
}
