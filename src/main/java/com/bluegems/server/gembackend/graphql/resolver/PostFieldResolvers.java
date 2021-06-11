package com.bluegems.server.gembackend.graphql.resolver;

import com.bluegems.server.gembackend.dao.CommentDao;
import com.bluegems.server.gembackend.dao.PostDao;
import com.bluegems.server.gembackend.dao.UserDao;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.graphql.model.Comment;
import com.bluegems.server.gembackend.graphql.model.Post;
import com.bluegems.server.gembackend.graphql.utils.EntityToModel;
import com.bluegems.server.gembackend.security.GemUserDetails;
import com.coxautodev.graphql.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PostFieldResolvers implements GraphQLResolver<Post> {

    @Autowired
    private UserDao userDao;

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private PostDao postDao;

    @PreAuthorize("isAuthenticated()")
    public List<Comment> comments(Post post) {
        try {
        String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            return commentDao.getPostComments(currentUser, post.getId()).stream().map(EntityToModel::fromCommentEntity).collect(Collectors.toList());
        } catch (Exception exception) {
            log.error("Failed to fetch comments for post ID "+post.getId(), exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching comments for post");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public Boolean isLiked(Post post) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            return postDao.isLiked(currentUser, post.getId());
        } catch (Exception exception) {
            log.error("Failed to fetch isLiked for post ID "+post.getId(), exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching isLiked for post");
        }
    }
}
