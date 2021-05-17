package com.bluegems.server.gembackend.graphql.resolver;

import com.bluegems.server.gembackend.dao.FriendshipDao;
import com.bluegems.server.gembackend.dao.UserDao;
import com.bluegems.server.gembackend.entity.FriendshipEntity;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.graphql.model.Friendship;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FriendshipResolver implements GraphQLMutationResolver {

    @Autowired
    private UserDao userDao;

    @Autowired
    private FriendshipDao friendshipDao;

//    @PreAuthorize("isAuthenticated()")
//    public List<User> getFriends(String requestedUserUsername, String requestedUserTag) {
//        try {
//            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
//            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
//            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
//            return friendshipDao.fetchFriendsByUser(currentUser, requestedUser).stream().map(EntityToModel::fromUserEntity).collect(Collectors.toList());
//        } catch (Exception exception) {
//            log.error("Failed to fetch friends for user "+requestedUserUsername+"#"+requestedUserTag, exception);
//            if (exception instanceof ThrowableGemGraphQLException) throw exception;
//            else throw new ThrowableGemGraphQLException("Server encountered error while fetching user friends");
//        }
//    }

    @PreAuthorize("isAuthenticated()")
    public Friendship requestFriendship(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            return EntityToModel.fromFriendshipEntity(friendshipDao.requestFriendship(currentUser, requestedUser), currentUser, requestedUser);
        } catch (Exception exception) {
            log.error("Failed to send a friend request to user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while sending friend request");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public Friendship acceptFriendship(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            return EntityToModel.fromFriendshipEntity(friendshipDao.acceptFriendship(currentUser, requestedUser), currentUser, requestedUser);
        } catch (Exception exception) {
            log.error("Failed to accept friend request from user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while accepting friend request");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public Friendship declineFriendship(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            return EntityToModel.fromFriendshipEntity(friendshipDao.declineFriendship(currentUser, requestedUser), currentUser, requestedUser);
        } catch (Exception exception) {
            log.error("Failed to decline friend request from user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while declining friend request");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public Friendship deleteRequest(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            friendshipDao.deleteRequest(currentUser, requestedUser);
            return EntityToModel.fromFriendshipEntity(null, null, null);
        } catch (Exception exception) {
            log.error("Failed to delete friend request to user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while deleting friend request");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public Friendship blockUser(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            return EntityToModel.fromFriendshipEntity(friendshipDao.blockUser(currentUser, requestedUser), currentUser, requestedUser);
        } catch (Exception exception) {
            log.error("Failed to block user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while blocking user");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public Friendship unblockUser(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            friendshipDao.unblockUser(currentUser, requestedUser);
            return EntityToModel.fromFriendshipEntity(null, null, null);
        } catch (Exception exception) {
            log.error("Failed to unblock user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while unblocking user");
        }
    }

    @PreAuthorize("isAuthenticated")
    public Friendship unfriendUser(String requestedUserUsername, String requestedUserTag) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(requestedUserUsername, requestedUserTag);
            friendshipDao.unfriendUser(currentUser, requestedUser);
            return EntityToModel.fromFriendshipEntity(null, null, null);
        } catch (Exception exception) {
            log.error("Failed to unblock user "+requestedUserUsername+"#"+requestedUserTag, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while unblocking user");
        }
    }
}
