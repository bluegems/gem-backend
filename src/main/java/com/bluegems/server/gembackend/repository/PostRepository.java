package com.bluegems.server.gembackend.repository;

import com.bluegems.server.gembackend.entity.PostEntity;
import com.bluegems.server.gembackend.entity.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @NotNull
    @Override
    @Query(value = "FROM post p JOIN FETCH p.userEntity WHERE p.id=:id")
    Optional<PostEntity> findById(@NotNull Long id);

    @Query(value = "SELECT p FROM post p JOIN FETCH p.userEntity WHERE p.userEntity=:user ORDER BY p.created DESC")
    List<PostEntity> fetchPostsByUser(@Param("user") UserEntity user);

    @Query(value = "SELECT p FROM post p JOIN FETCH p.userEntity WHERE p.userEntity IN :users ORDER BY p.created DESC")
    List<PostEntity> fetchPostsFromUsers(@Param("users") List<UserEntity> users);
}
