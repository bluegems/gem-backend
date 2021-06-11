package com.bluegems.server.gembackend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostLikeId implements Serializable {

    private Long postEntity;
    private Long userEntity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostLikeId that = (PostLikeId) o;
        return postEntity.equals(that.postEntity) && userEntity.equals(that.userEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postEntity, userEntity);
    }
}
