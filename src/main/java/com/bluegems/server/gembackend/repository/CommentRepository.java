package com.bluegems.server.gembackend.repository;

import com.bluegems.server.gembackend.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query(value = "SELECT c FROM comment c JOIN FETCH c.userEntity JOIN FETCH c.postEntity cp JOIN FETCH cp.userEntity WHERE cp.id=:postId")
    List<CommentEntity> fetchCommentsByPostId(@Param("postId") Long postId);
}
