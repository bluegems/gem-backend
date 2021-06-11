package com.bluegems.server.gembackend.graphql.resolver;

import com.bluegems.server.gembackend.dao.FriendshipDao;
import com.bluegems.server.gembackend.dao.PostDao;
import com.bluegems.server.gembackend.dao.UserDao;
import com.bluegems.server.gembackend.entity.FriendshipEntity;
import com.bluegems.server.gembackend.entity.PostEntity;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.graphql.model.Friendship;
import com.bluegems.server.gembackend.graphql.model.Post;
import com.bluegems.server.gembackend.graphql.model.User;
import com.bluegems.server.gembackend.graphql.utils.EntityToModel;
import com.bluegems.server.gembackend.security.GemUserDetails;
import com.coxautodev.graphql.tools.GraphQLResolver;
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
public class UserFieldResolvers implements GraphQLResolver<User> {

    @Autowired
    private FriendshipDao friendshipDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;

    @PreAuthorize("isAuthenticated()")
    public Friendship friendship(User user) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            boolean sameUser = Boolean.logicalAnd(
                    user.getUsername().equals(currentUser.getUsername()),
                    user.getTag().equals(currentUser.getTag())
            );
            if (sameUser) {
                return EntityToModel.fromFriendshipEntity(null, null, null);
            }
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(user.getUsername(), user.getTag());
            Optional<FriendshipEntity> friendship = friendshipDao.fetchOptionalFriendshipByUsers(currentUser, requestedUser);
            return EntityToModel.fromFriendshipEntity(friendship.orElse(null), currentUser, requestedUser);
        } catch (Exception exception) {
            log.error("Failed to fetch friendship " + user.getUsername() + "#" + user.getTag(), exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching friendship");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public List<User> friendRequests(User user) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            return friendshipDao.fetchFriendRequestsForUser(currentUser).stream().map(EntityToModel::fromUserEntity).collect(Collectors.toList());
        } catch (Exception exception) {
            log.error("Failed to fetch pending requests for user", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching pending requests");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public List<User> friends(User user) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(user.getUsername(), user.getTag());
            List<UserEntity> friendsEntityList = friendshipDao.fetchFriendsByUser(currentUser, requestedUser);
            return (friendsEntityList != null) ?
                    friendsEntityList.stream().map(EntityToModel::fromUserEntity).collect(Collectors.toList()) :
                    null;
        } catch (Exception exception) {
            log.error("Failed to fetch friends for " + user.getUsername() + "#" + user.getTag(), exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching friends for user");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public List<Post> posts(User user) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            UserEntity requestedUser = userDao.fetchUserByUsernameAndTag(user.getUsername(), user.getTag());
            List<PostEntity> postsEntityList = postDao.fetchPostsByUser(currentUser, requestedUser);
            return (postsEntityList != null) ?
                    postsEntityList.stream().map(EntityToModel::fromPostEntity).collect(Collectors.toList()) :
                    null;
        } catch (Exception exception) {
            log.error("Failed to fetch posts for " + user.getUsername() + "#" + user.getTag(), exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching posts for user");
        }
    }
}
