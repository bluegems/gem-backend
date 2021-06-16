package com.bluegems.server.gembackend.dao;

import com.bluegems.server.gembackend.entity.PostEntity;
import com.bluegems.server.gembackend.entity.PostLikeEntity;
import com.bluegems.server.gembackend.entity.PostLikeId;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.repository.FriendshipRepository;
import com.bluegems.server.gembackend.repository.PostLikeRepository;
import com.bluegems.server.gembackend.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class PostDao {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    public PostEntity fetchPostById(UserEntity currentUser, Long id) {
        List<UserEntity> currentUserFriends = friendshipRepository.fetchFriendsByUser(currentUser);
        Optional<PostEntity> post = postRepository.findById(id);
        if (post.isEmpty()) {
            throw new ThrowableGemGraphQLException("Post with this ID not found");
        }
        boolean byUserOrFriend = Boolean.logicalOr(
                post.get().getUserEntity().equals(currentUser),
                currentUserFriends.contains(post.get().getUserEntity())
        );
        if (!byUserOrFriend) {
            throw new ThrowableGemGraphQLException("Unauthorized to see this user's posts");
        }
        return post.get();
    }

    public List<PostEntity> fetchPostsByUser(UserEntity currentUser, UserEntity requestedUser) {
        List<UserEntity> currentUserFriends = friendshipRepository.fetchFriendsByUser(currentUser);
        boolean userOrFriend = Boolean.logicalOr(
                requestedUser.equals(currentUser),
                currentUserFriends.contains(requestedUser)
        );
        if (!userOrFriend) {
            log.warn("Cannot fetch posts from stranger");
            return null;
        }
        return postRepository.fetchPostsByUser(requestedUser);
    }

    public List<PostEntity> fetchFriendsPosts(UserEntity currentUser) {
        List<UserEntity> currentUserFriends = friendshipRepository.fetchFriendsByUser(currentUser);
        return postRepository.fetchPostsFromUsers(currentUserFriends);
    }

    public PostEntity createPost(UserEntity currentUser, String description, String image) {
        PostEntity postEntity = PostEntity.builder()
                .description(description)
                .image(image)
                .userEntity(currentUser)
                .build();
        return postRepository.save(postEntity);
    }

    public PostEntity likePost(UserEntity currentUser, Long postId) {
        Optional<PostEntity> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new ThrowableGemGraphQLException("Post with this ID does not exist");
        }
        UserEntity otherUser = post.get().getUserEntity();
        if (!currentUser.equals(otherUser) && !friendshipRepository.isFriend(
                currentUser.isLessThan(otherUser) ? currentUser : otherUser,
                currentUser.isGreaterThan(otherUser) ? currentUser : otherUser
        )) {
            log.warn("Cannot like a stranger's post");
            throw new ThrowableGemGraphQLException("Cannot like a stranger's post");
        }
        PostLikeEntity postLikeEntity = PostLikeEntity.builder()
                .postEntity(post.get())
                .userEntity(currentUser)
                .build();
        postLikeRepository.save(postLikeEntity);
        return postRepository.findById(postId).orElseThrow(() -> new ThrowableGemGraphQLException("Post with this ID does not exist"));
    }

    public PostEntity unlikePost(UserEntity currentUser, Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ThrowableGemGraphQLException("Post with this ID does not exist");
        }
        Optional<PostLikeEntity> postLike = postLikeRepository.findById(PostLikeId.builder()
                .postEntity(postId)
                .userEntity(currentUser.getId())
                .build());
        if (postLike.isEmpty()) {
            assert true; // NO OP
        } else {
            postLikeRepository.delete(postLike.get());
        }
        return postRepository.findById(postId).orElseThrow(() -> new ThrowableGemGraphQLException("Post with this ID does not exist"));
    }

    public Boolean isLiked(UserEntity currentUser, Long postId) {
        Optional<PostEntity> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            throw new ThrowableGemGraphQLException("Post with this ID does not exist");
        }
        return postLikeRepository.existsById(PostLikeId.builder().postEntity(postId).userEntity(currentUser.getId()).build());
    }
}
