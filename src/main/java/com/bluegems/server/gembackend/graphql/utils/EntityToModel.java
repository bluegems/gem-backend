package com.bluegems.server.gembackend.graphql.utils;

import com.bluegems.server.gembackend.entity.*;
import com.bluegems.server.gembackend.graphql.model.*;

public class EntityToModel {

    public static User fromUserEntity(UserEntity userEntity) {
        if (userEntity==null) return null;
        return User.builder()
                .tag(userEntity.getTag())
                .username(userEntity.getUsername())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .bio(userEntity.getBio())
                .birthdate(userEntity.getBirthdate())
                .profilePicture(userEntity.getProfilePicture())
                .build();
    }

    public static Account fromAccountEntity(AccountEntity accountEntity) {
        return Account.builder()
                .id(accountEntity.getId())
                .email(accountEntity.getEmail())
                .build();
    }

    public static Friendship fromFriendshipEntity(FriendshipEntity friendshipEntity, UserEntity currentUser, UserEntity otherUser) {
        return (friendshipEntity==null) ?
                Friendship.builder().exists(false).build() :
                Friendship.builder()
                        .exists(true)
                        .currentUser(fromUserEntity(currentUser))
                        .otherUser(fromUserEntity(otherUser))
                        .status(friendshipEntity.getStatus())
                        .modifiedBy(fromUserEntity(friendshipEntity.getModifiedBy()))
                        .build();
    }

    public static Post fromPostEntity(PostEntity postEntity) {
        return Post.builder()
                .id(postEntity.getId())
                .description(postEntity.getDescription())
                .image(postEntity.getImage())
                .user(fromUserEntity(postEntity.getUserEntity()))
                .modifiedDatetime(postEntity.getModified())
                .build();
    }

    public static Comment fromCommentEntity(CommentEntity commentEntity) {
        return Comment.builder()
                .id(commentEntity.getId())
                .text(commentEntity.getText())
                .datetime(commentEntity.getCreated())
                .user(fromUserEntity(commentEntity.getUserEntity()))
                .post(fromPostEntity(commentEntity.getPostEntity()))
                .build();
    }
}
