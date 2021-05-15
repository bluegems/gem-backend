package com.bluegems.server.gembackend.graphql.resolver;

import com.bluegems.server.gembackend.dao.FriendshipDao;
import com.bluegems.server.gembackend.dao.UserDao;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.graphql.model.User;
import com.bluegems.server.gembackend.graphql.utils.EntityToModel;
import com.bluegems.server.gembackend.security.GemUserDetails;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FriendshipResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private UserDao userDao;

    @Autowired
    private FriendshipDao friendshipDao;

    @PreAuthorize("isAuthenticated()")
    public List<User> getFriends(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            return friendshipDao.fetchFriendsByUser(currentUser, requestedUser).stream().map(EntityToModel::fromUserEntity).collect(Collectors.toList());
        } catch (Exception exception) {
            log.error("Failed to fetch friends for user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching user friends");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public void requestFriendship(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            friendshipDao.requestFriendship(currentUser, requestedUser);
        } catch (Exception exception) {
            log.error("Failed to send a friend request to user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while sending friend request");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public void acceptFriendship(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            friendshipDao.acceptFriendship(currentUser, requestedUser);
        } catch (Exception exception) {
            log.error("Failed to accept friend request from user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while accepting friend request");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public void declineFriendship(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            friendshipDao.declineFriendship(currentUser, requestedUser);
        } catch (Exception exception) {
            log.error("Failed to decline friend request from user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while declining friend request");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public void deleteRequest(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            friendshipDao.deleteRequest(currentUser, requestedUser);
        } catch (Exception exception) {
            log.error("Failed to delete friend request to user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while deleting friend request");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public void blockUser(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            friendshipDao.blockUser(currentUser, requestedUser);
        } catch (Exception exception) {
            log.error("Failed to block user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while blocking user");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public void unblockUser(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            friendshipDao.unblockUser(currentUser, requestedUser);
        } catch (Exception exception) {
            log.error("Failed to unblock user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while unblocking user");
        }
    }
}
