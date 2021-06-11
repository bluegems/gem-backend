package com.bluegems.server.gembackend.graphql.resolver;

import com.bluegems.server.gembackend.dao.AccountDao;
import com.bluegems.server.gembackend.dao.UserDao;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.graphql.model.ImageInput;
import com.bluegems.server.gembackend.graphql.model.User;
import com.bluegems.server.gembackend.graphql.utils.EntityToModel;
import com.bluegems.server.gembackend.security.GemUserDetails;
import com.bluegems.server.gembackend.security.GemUserDetailsService;
import com.bluegems.server.gembackend.security.jwt.JWTOperations;
import com.bluegems.server.gembackend.service.ImgurService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.github.kskelm.baringo.model.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class UserResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private ImgurService imgurService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private GemUserDetailsService gemUserDetailsService;

    @Autowired
    private JWTOperations jwtOperations;

    @PreAuthorize("isAuthenticated()")
    public User getUser(String username, String tag) {
        try {
            return EntityToModel.fromUserEntity(userDao.fetchUserByUsernameAndTag(username, tag));
        } catch (Exception exception) {
            log.error("Failed to fetch user details", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching user details");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public User getCurrentUser() {
        try {
            String email = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            return EntityToModel.fromUserEntity(userDao.fetchUserByEmail(email));
        } catch (Exception exception) {
            log.error("Failed to fetch user details from token", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else
                throw new ThrowableGemGraphQLException("Server encountered error while fetching user details from token");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public List<User> searchUsers(String searchString) {
        try {
            return userDao.searchUsers(searchString).stream().map(EntityToModel::fromUserEntity).collect(Collectors.toList());
        } catch (Exception exception) {
            log.error("Failed to search users", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else
                throw new ThrowableGemGraphQLException("Server encountered error while searching users");
        }
    }

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public User updateUser(String username, String tag, String firstName, String lastName, String bio, LocalDate birthdate, ImageInput profilePicture, Boolean keepPreviousPicture) {
        try {
            String email = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(email);
            // Keep previous profile picture by default
            String imgurImageId = currentUser.getProfilePicture();
            if (!keepPreviousPicture)
                imgurImageId = imgurService.deleteAndUploadImage(currentUser.getProfilePicture(), profilePicture);
            UserEntity userEntity = userDao.updateUser(currentUser.getUsername(), currentUser.getTag(), username, tag, firstName, lastName, bio, birthdate, imgurImageId);
            return EntityToModel.fromUserEntity(userEntity);
        } catch (Exception exception) {
            log.error("Failed to update user details", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while updating user");
        }
    }
}
