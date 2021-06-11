package com.bluegems.server.gembackend.graphql.resolver;

import com.bluegems.server.gembackend.dao.CommentDao;
import com.bluegems.server.gembackend.dao.FriendshipDao;
import com.bluegems.server.gembackend.dao.UserDao;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.graphql.model.Comment;
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
public class CommentResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private UserDao userDao;

    @PreAuthorize("isAuthenticated()")
    public List<Comment> getPostComments(Long postId) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            return commentDao.getPostComments(currentUser, postId).stream().map(EntityToModel::fromCommentEntity).collect(Collectors.toList());
        } catch (Exception exception) {
            log.error("Failed to fetch post comments", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching post comments");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public Comment createComment(String text, Long postId) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            return EntityToModel.fromCommentEntity(commentDao.createComment(currentUser, postId, text));
        } catch (Exception exception) {
            log.error("Failed to create comment", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while creating comment");
        }
    }
}
