package com.bluegems.server.gembackend.graphql.resolver;

import com.bluegems.server.gembackend.dao.AccountDao;
import com.bluegems.server.gembackend.dao.UserDao;
import com.bluegems.server.gembackend.entity.AccountEntity;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.graphql.model.User;
import com.bluegems.server.gembackend.graphql.utils.EntityToModel;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static com.bluegems.server.gembackend.graphql.utils.CommonUtils.createTag;

@Slf4j
@Component
public class UserResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    public User getUser(String username, String tag) {
        try {
            return EntityToModel.fromUserEntity(userDao.fetchUserByUsernameAndTag(username, tag));
        } catch (Exception exception) {
            log.error("Failed to fetch user details", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching user details");
        }
    }

    public User createUser(UUID accountId, String username, String firstName, String lastName, String bio, LocalDate birthdate, String profilePicture) {
        try {
            UserEntity userEntity = userDao.createUser(accountId, username, createTag(4), firstName, lastName, bio, birthdate, profilePicture);
            return EntityToModel.fromUserEntity(userEntity);
        } catch (Exception exception) {
            log.error("Failed to create user", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while creating user");
        }
    }

    public User createAccountAndUser(String email, String password, String username, String firstName, String lastName, String bio, LocalDate birthdate, String profilePicture) {
        try {
            AccountEntity accountEntity = accountDao.createAccount(email, password);
            UserEntity userEntity = userDao.createUser(accountEntity.getId(), username, createTag(4), firstName, lastName, bio, birthdate, profilePicture);
            return EntityToModel.fromUserEntity(userEntity);
        } catch (Exception exception) {
            log.error("Failed to create user", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while creating user");
        }
    }

    public User updateUser(String username, String tag, String firstName, String lastName, String bio, LocalDate birthdate, String profilePicture) {
        try {
            UserEntity userEntity = userDao.updateUser(username, tag, firstName, lastName, bio, birthdate, profilePicture);
            return EntityToModel.fromUserEntity(userEntity);
        } catch (Exception exception) {
            log.error("Failed to update user details", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while updating user");
        }
    }
}
