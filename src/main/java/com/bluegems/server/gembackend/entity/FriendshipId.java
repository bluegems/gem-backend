package com.bluegems.server.gembackend.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
public class FriendshipId implements Serializable {

    private Long userOne;
    private Long userTwo;

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (obj==null || getClass() != obj.getClass()) return false;
        FriendshipId friendshipId = (FriendshipId) obj;
        return userOne.equals(friendshipId.userOne) && userTwo.equals(friendshipId.userTwo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userOne, userTwo);
    }
}
