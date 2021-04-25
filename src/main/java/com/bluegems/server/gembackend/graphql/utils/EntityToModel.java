package com.bluegems.server.gembackend.graphql.utils;

import com.bluegems.server.gembackend.entity.AccountEntity;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.graphql.model.Account;
import com.bluegems.server.gembackend.graphql.model.User;

public class EntityToModel {

    public static User fromUserEntity(UserEntity userEntity) {
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
}
