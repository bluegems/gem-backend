package com.bluegems.server.gembackend.dao;

import com.bluegems.server.gembackend.entity.AccountEntity;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.GemGraphQLErrorExtensions;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.repository.AccountRepository;
import com.bluegems.server.gembackend.repository.UserRepository;
import com.bluegems.server.gembackend.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class UserDao {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    public UserEntity fetchUserByUsernameAndTag(String username, String tag) {
        Optional<UserEntity> user = userRepository.fetchUserByUsernameAndTag(username, tag);
        if (user.isEmpty()) {
            log.warn("User not found {}#{}", username, tag);
            throw new ThrowableGemGraphQLException(
                    "User with the specified username and tag not found",
                    GemGraphQLErrorExtensions.builder().invalidField("username").build()
            );
        }
        return user.get();
    }

    public UserEntity fetchUserByEmail(String email) {
        Optional<UserEntity> user = userRepository.fetchUserByEmail(email);
        if (user.isEmpty()) {
            log.warn("User not found with email {}", email);
            throw new ThrowableGemGraphQLException(
                    "User with the specified email not found",
                    GemGraphQLErrorExtensions.builder().invalidField("email").build()
            );
        }
        return user.get();
    }

    public UserEntity createUser(UUID accountId, String username, String firstName, String lastName, String bio, LocalDate birthDate, String profilePicture) {
        Optional<AccountEntity> associatedAccount = accountRepository.findById(accountId);
        if (associatedAccount.isEmpty()) {
            log.warn("Account info not found for id {}", accountId);
            throw new ThrowableGemGraphQLException(
                    "Account ID not found. Please sign up first",
                    GemGraphQLErrorExtensions.builder().invalidField("accountId").build()
            );
        }
        String tag = CommonUtils.createTag(4);
        while (userRepository.fetchUserByUsernameAndTag(username, tag).isPresent()) {
            tag = CommonUtils.createTag(4);
        }
        UserEntity userEntity = UserEntity.builder()
                .accountEntity(associatedAccount.get())
                .firstName(firstName)
                .username(username)
                .tag(tag)
                .build();
        userEntity.setBirthdate(birthDate);
        userEntity.setBio((bio != null && !bio.isEmpty()) ? bio : null);
        userEntity.setLastName((lastName != null && !lastName.isEmpty()) ? lastName : null);
        userEntity.setProfilePicture((profilePicture != null && !profilePicture.isEmpty()) ? profilePicture : null);
        return userRepository.saveAndFlush(userEntity);
    }

    public UserEntity updateUser(String username, String tag, String firstName, String lastName, String bio, LocalDate birthdate, String profilePicture) {
        Optional<UserEntity> foundUser = userRepository.fetchUserByUsernameAndTag(username, tag);
        if (foundUser.isEmpty()) {
            log.warn("User not found {}#{}", username, tag);
            throw new ThrowableGemGraphQLException(
                    "User with the specified username and tag not found",
                    GemGraphQLErrorExtensions.builder().invalidField("username").build()
            );
        }
        UserEntity userEntity = foundUser.get();

        userEntity.setFirstName((firstName != null && !firstName.isEmpty()) ? firstName : userEntity.getFirstName());
        userEntity.setLastName(lastName);
        userEntity.setBio(bio);
        userEntity.setBirthdate(birthdate);
        userEntity.setProfilePicture(profilePicture);
        return userRepository.saveAndFlush(userEntity);
    }
}
