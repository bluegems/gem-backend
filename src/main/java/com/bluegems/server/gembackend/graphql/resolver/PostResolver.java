package com.bluegems.server.gembackend.graphql.resolver;

import com.bluegems.server.gembackend.dao.PostDao;
import com.bluegems.server.gembackend.dao.UserDao;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.graphql.model.Post;
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
public class PostResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserDao userDao;

    @PreAuthorize("isAuthenticated()")
    public Post getPost(Long id) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            return EntityToModel.fromPostEntity(postDao.fetchPostById(currentUser, id));
        } catch (Exception exception) {
            log.error("Failed to fetch post with ID"+id, exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching post");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public List<Post> getFriendsPosts() {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            return postDao.fetchFriendsPosts(currentUser).stream().map(EntityToModel::fromPostEntity).collect(Collectors.toList());
        } catch (Exception exception) {
            log.error("Failed to fetch friends posts", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while fetching friends posts");
        }
    }

    @PreAuthorize("isAuthenticated()")
    public Post createPost(String description, String image) {
        try {
            String currentUserEmail = ((GemUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            UserEntity currentUser = userDao.fetchUserByEmail(currentUserEmail);
            return EntityToModel.fromPostEntity(postDao.createPost(currentUser, description, image));
        } catch (Exception exception) {
            log.error("Failed to create post", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while creating post");
        }
    }
}
