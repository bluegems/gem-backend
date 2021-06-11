package com.bluegems.server.gembackend.repository;

import com.bluegems.server.gembackend.entity.PostLikeEntity;
import com.bluegems.server.gembackend.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLikeEntity, PostLikeId> {
}
