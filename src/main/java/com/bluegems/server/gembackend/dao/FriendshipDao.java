package com.bluegems.server.gembackend.dao;

import com.bluegems.server.gembackend.entity.FriendshipEntity;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.GemGraphQLErrorExtensions;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.repository.FriendshipRepository;
import com.bluegems.server.gembackend.repository.UserRepository;
import com.bluegems.server.gembackend.utils.FriendshipStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// TODO: Serious refactoring, a lot of repetition I'd like to reduce somehow
// TODO: Pick a style, stick to it
@Slf4j
@Component
public class FriendshipDao {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    private List<UserEntity> powerpuffGirls;

    @PostConstruct
    public void init() {
        powerpuffGirls = userRepository.fetchPowerpuffGirls();
    }

    public void addDefaultFriends(UserEntity userEntity) {
        List<FriendshipEntity> defaultFriendships = powerpuffGirls.stream().map(
                girl -> {
                    FriendshipEntity friendshipEntity = createBaseFriendship(girl, userEntity);
                    friendshipEntity.setStatus(FriendshipStatus.ACCEPTED.toString());
                    friendshipEntity.setModifiedBy(girl);
                    return friendshipEntity;
                }
        ).collect(Collectors.toList());
        friendshipRepository.saveAll(defaultFriendships);
    }

    public List<UserEntity> fetchFriendsByUser(UserEntity currentUser, UserEntity otherUser) {
        // The authorized users must only be able to view their friends or their friends' friends (Apart from the default friends)
        boolean userOrFriend = Boolean.logicalOr(
                currentUser.equals(otherUser),
                isFriend(currentUser, otherUser, true)
        );
        if (!userOrFriend) {
            log.warn("Cannot view a powerpuff girl's or a stranger's friend list");
            return null;
//            throw new ThrowableGemGraphQLException("Cannot view a powerpuff girl's or a stranger's friend list");
        }
        return friendshipRepository.fetchFriendsByUser(otherUser);
    }

    public Boolean isFriend(UserEntity currentUser, UserEntity otherUser, Boolean ignorePowerpuffGirls) {
        return Boolean.logicalAnd(
                (!ignorePowerpuffGirls || !powerpuffGirls.contains(otherUser)),
                friendshipRepository.isFriend(
                        currentUser.isLessThan(otherUser) ? currentUser : otherUser,
                        currentUser.isGreaterThan(otherUser) ? currentUser : otherUser
                )
        );
    }

    public Optional<FriendshipEntity> fetchOptionalFriendshipByUsers(UserEntity userOne, UserEntity userTwo) {
        return friendshipRepository.findFriendshipByUsers(
                userOne.isLessThan(userTwo) ? userOne : userTwo,
                userOne.isGreaterThan(userTwo) ? userOne : userTwo
        );
    }

    public FriendshipEntity fetchFriendshipByUsers(UserEntity userOne, UserEntity userTwo) {
        Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByUsers(
                userOne.isLessThan(userTwo) ? userOne : userTwo,
                userOne.isGreaterThan(userTwo) ? userOne : userTwo
        );
        if (friendship.isEmpty()) {
            log.warn("Friendship not found between users {}#{} and {}#{}", userOne.getUsername(), userOne.getTag(), userTwo.getUsername(), userTwo.getTag());
            throw new ThrowableGemGraphQLException(
                    "Friendship not found between users",
                    GemGraphQLErrorExtensions.builder().invalidField("username, tag").build()
            );
        }
        return friendship.get();
    }

    public FriendshipEntity requestFriendship(UserEntity currentUser, UserEntity otherUser) {
        Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByUsers(
                currentUser.isLessThan(otherUser) ? currentUser : otherUser,
                currentUser.isGreaterThan(otherUser) ? currentUser : otherUser
        );
        if (friendship.isPresent()) {
            FriendshipEntity friendshipEntity = friendship.get();
            if (!canRequestFriendship(currentUser, otherUser, friendshipEntity)) {
                throw new ThrowableGemGraphQLException("An older friend request for this user is open or has been declined");
            } else {
                friendshipEntity.setStatus(FriendshipStatus.REQUESTED.toString());
                friendshipEntity.setModifiedBy(currentUser);
                return friendshipRepository.save(friendshipEntity);
            }
        } else {
            FriendshipEntity friendshipEntity = createBaseFriendship(currentUser, otherUser);
            friendshipEntity.setStatus(FriendshipStatus.REQUESTED.toString());
            friendshipEntity.setModifiedBy(currentUser);
            return friendshipRepository.save(friendshipEntity);
        }
    }

    public FriendshipEntity acceptFriendship(UserEntity currentUser, UserEntity otherUser) {
        Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByUsers(
                currentUser.isLessThan(otherUser) ? currentUser : otherUser,
                currentUser.isGreaterThan(otherUser) ? currentUser : otherUser
        );
        if (friendship.isEmpty() || !canAcceptOrDeclineFriendship(currentUser, otherUser, friendship.get())) {
            log.warn("No open friend request found from user {}", otherUser);
            throw new ThrowableGemGraphQLException("No open friend request found from this user");
        }
        FriendshipEntity modifiedFriendship = friendship.get();
        modifiedFriendship.setStatus(FriendshipStatus.ACCEPTED.toString());
        modifiedFriendship.setModifiedBy(currentUser);
        return friendshipRepository.save(modifiedFriendship);
    }

    public FriendshipEntity declineFriendship(UserEntity currentUser, UserEntity otherUser) {
        Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByUsers(
                currentUser.isLessThan(otherUser) ? currentUser : otherUser,
                currentUser.isGreaterThan(otherUser) ? currentUser : otherUser
        );
        if (friendship.isEmpty() || !canAcceptOrDeclineFriendship(currentUser, otherUser, friendship.get())) {
            log.warn("No open friend request found between users {} and {}", currentUser, otherUser);
            throw new ThrowableGemGraphQLException("No open friend request found between users");
        }
        FriendshipEntity modifiedFriendship = friendship.get();
        modifiedFriendship.setStatus(FriendshipStatus.DECLINED.toString());
        modifiedFriendship.setModifiedBy(currentUser);
        return friendshipRepository.save(modifiedFriendship);
    }

    public void deleteRequest(UserEntity currentUser, UserEntity otherUser) {
        Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByUsers(
                currentUser.isLessThan(otherUser) ? currentUser : otherUser,
                currentUser.isGreaterThan(otherUser) ? currentUser : otherUser
        );
        if (friendship.isEmpty() || !canDeleteRequest(currentUser, otherUser, friendship.get())) {
            log.warn("No open friend request from {}#{} to {}#{}", currentUser.getUsername(), currentUser.getTag(), otherUser.getUsername(), otherUser.getTag());
            throw new ThrowableGemGraphQLException("No open friend request to this user");
        }
        friendshipRepository.delete(friendship.get());
    }

    public FriendshipEntity blockUser(UserEntity currentUser, UserEntity otherUser) {
        Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByUsers(
                currentUser.isLessThan(otherUser) ? currentUser : otherUser,
                currentUser.isGreaterThan(otherUser) ? currentUser : otherUser
        );
        if (friendship.isPresent() && friendship.get().getStatus().equals(FriendshipStatus.BLOCKED.toString())) {
            return friendship.get();
        } else {
            FriendshipEntity friendshipEntity = friendship.orElseGet(() -> createBaseFriendship(currentUser, otherUser));
            friendshipEntity.setStatus(FriendshipStatus.BLOCKED.toString());
            friendshipEntity.setModifiedBy(currentUser);
            return friendshipRepository.save(friendshipEntity);
        }
    }

    public void unblockUser(UserEntity currentUser, UserEntity otherUser) {
        Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByUsers(
                currentUser.isLessThan(otherUser) ? currentUser : otherUser,
                currentUser.isGreaterThan(otherUser) ? currentUser : otherUser
        );
        if (friendship.isEmpty()) {
            log.warn("There is no block on {}#{} from {}#{}", currentUser.getUsername(), currentUser.getTag(), otherUser.getUsername(), otherUser.getTag());
            throw new ThrowableGemGraphQLException("There is no block on this user");
        } else if (!canUnblock(currentUser, otherUser, friendship.get())) {
            log.warn("Unauthorized to unblock");
            throw new ThrowableGemGraphQLException("This block can only be lifted by the other user");
        } else {
            FriendshipEntity friendshipEntity = friendship.get();
            friendshipRepository.delete(friendshipEntity);
        }
    }

    public void unfriendUser(UserEntity currentUser, UserEntity otherUser) {
        Optional<FriendshipEntity> friendship = friendshipRepository.findFriendshipByUsers(
                currentUser.isLessThan(otherUser) ? currentUser : otherUser,
                currentUser.isGreaterThan(otherUser) ? currentUser : otherUser
        );
        if (friendship.isEmpty() || !canUnfriend(currentUser, otherUser, friendship.get())) {
            log.warn("These users are not friends");
            throw new ThrowableGemGraphQLException("This user is not a friend");
        }
        friendshipRepository.delete(friendship.get());
    }

    private Boolean canRequestFriendship(UserEntity currentUser, UserEntity otherUser, FriendshipEntity friendshipEntity) {
        // The authorized user cannot initiate a friend request with another user if a request is already open or the other user has declined an older request
        return !Boolean.logicalOr(
                friendshipEntity.getStatus().equalsIgnoreCase(FriendshipStatus.REQUESTED.toString()) && friendshipEntity.getModifiedBy().equals(currentUser),
                friendshipEntity.getStatus().equalsIgnoreCase(FriendshipStatus.DECLINED.toString()) && friendshipEntity.getModifiedBy().equals(otherUser)
        );
    }

    private Boolean canAcceptOrDeclineFriendship(UserEntity currentUser, UserEntity otherUser, FriendshipEntity friendshipEntity) {
        // The authorized user can only accept or decline a request that is in REQUESTED state initiated by the other user
        return Boolean.logicalAnd(
                friendshipEntity.getStatus().equalsIgnoreCase(FriendshipStatus.REQUESTED.toString()),
                friendshipEntity.getModifiedBy().equals(otherUser)
        );
    }

    private Boolean canDeleteRequest(UserEntity currentUser, UserEntity otherUser, FriendshipEntity friendshipEntity) {
        return Boolean.logicalAnd(
                friendshipEntity.getStatus().equalsIgnoreCase(FriendshipStatus.REQUESTED.toString()),
                friendshipEntity.getModifiedBy().equals(currentUser)
        );
    }

    private Boolean canUnblock(UserEntity currentUser, UserEntity otherUser, FriendshipEntity friendshipEntity) {
        return Boolean.logicalAnd(
                friendshipEntity.getStatus().equalsIgnoreCase(FriendshipStatus.BLOCKED.toString()),
                friendshipEntity.getModifiedBy().equals(currentUser)
        );
    }

    private Boolean canUnfriend(UserEntity currentUser, UserEntity otherUser, FriendshipEntity friendshipEntity) {
        return friendshipEntity.getStatus().equals(FriendshipStatus.ACCEPTED.toString());
    }

    private FriendshipEntity createBaseFriendship(UserEntity userOne, UserEntity userTwo) {
        return FriendshipEntity.builder()
                .userOne(userOne.isLessThan(userTwo) ? userOne : userTwo)
                .userTwo(userOne.isGreaterThan(userTwo) ? userOne : userTwo)
                .build();
    }

}
