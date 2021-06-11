package com.bluegems.server.gembackend.dao;

import com.bluegems.server.gembackend.entity.CommentEntity;
import com.bluegems.server.gembackend.entity.PostEntity;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.repository.CommentRepository;
import com.bluegems.server.gembackend.repository.FriendshipRepository;
import com.bluegems.server.gembackend.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CommentDao {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private PostRepository postRepository;

    public List<CommentEntity> getPostComments(UserEntity currentUser, Long postId) {
        Optional<PostEntity> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new ThrowableGemGraphQLException("Post with this ID does not exist");
        }
        UserEntity otherUser = post.get().getUserEntity();
        if (!friendshipRepository.isFriend(
                currentUser.isLessThan(otherUser) ? currentUser : otherUser,
                currentUser.isGreaterThan(otherUser) ? currentUser : otherUser
        )) {
            log.warn("Cannot view a stranger's post's comments");
            return Collections.emptyList();
        }
        return commentRepository.fetchCommentsByPostId(postId);
    }

    public CommentEntity createComment(UserEntity currentUser, Long postId, String commentText) {
        Optional<PostEntity> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new ThrowableGemGraphQLException("Post with this ID does not exist");
        }
        UserEntity otherUser = post.get().getUserEntity();
        if (!friendshipRepository.isFriend(
                currentUser.isLessThan(otherUser) ? currentUser : otherUser,
                currentUser.isGreaterThan(otherUser) ? currentUser : otherUser
        )) {
            log.warn("Cannot comment on a stranger's post");
            throw new ThrowableGemGraphQLException("Cannot comment on a stranger's post");
        }
        CommentEntity newComment = CommentEntity.builder()
                .postEntity(post.get())
                .userEntity(currentUser)
                .text(commentText)
                .build();
        return commentRepository.save(newComment);
    }
}
