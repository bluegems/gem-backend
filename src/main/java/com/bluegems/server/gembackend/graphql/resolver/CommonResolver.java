package com.bluegems.server.gembackend.graphql.resolver;

import com.bluegems.server.gembackend.dao.AccountDao;
import com.bluegems.server.gembackend.dao.FriendshipDao;
import com.bluegems.server.gembackend.dao.UserDao;
import com.bluegems.server.gembackend.entity.AccountEntity;
import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.exception.graphql.GemGraphQLErrorExtensions;
import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;
import com.bluegems.server.gembackend.graphql.model.User;
import com.bluegems.server.gembackend.graphql.utils.EntityToModel;
import com.bluegems.server.gembackend.security.GemUserDetails;
import com.bluegems.server.gembackend.security.GemUserDetailsService;
import com.bluegems.server.gembackend.security.jwt.JWTOperations;
import com.bluegems.server.gembackend.utils.ValidationUtils;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
public class CommonResolver implements GraphQLMutationResolver {

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private FriendshipDao friendshipDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private GemUserDetailsService gemUserDetailsService;

    @Autowired
    private JWTOperations jwtOperations;

    @PreAuthorize("permitAll")
    public String login(String email, String password) {
        try {
            ValidationUtils.areNonEmptyNorNull(List.of(email, password));
            UsernamePasswordAuthenticationToken credentials = new UsernamePasswordAuthenticationToken(email, password);
            authenticationManager.authenticate(credentials);
            log.info("User logged in with email : {}", email);
        } catch (Exception exception) {
            log.error("Invalid credentials for log in", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else
                throw new ThrowableGemGraphQLException("Invalid credentials for login", new GemGraphQLErrorExtensions("email"));
        }
        final GemUserDetails gemUserDetails = gemUserDetailsService.loadUserByUsername(email);
        return jwtOperations.createToken(gemUserDetails);

    }

    @Transactional
    @PreAuthorize("permitAll")
    public User register(String email, String password, String username, String firstName, String lastName, String bio, String profilePicture) {
        try {
            ValidationUtils.areNonEmptyNorNull(List.of(email, password, username, firstName));
            AccountEntity accountEntity = accountDao.createAccount(email, password);
            log.info("Account created for email : {}", accountEntity.getEmail());
            UserEntity userEntity = userDao.createUser(accountEntity.getId(), username, firstName, lastName, bio, profilePicture);
            log.info("User profile created : {}#{}", userEntity.getUsername(), userEntity.getTag());
            friendshipDao.addDefaultFriends(userEntity);
            log.info("Default friends added for user : {}#{}", userEntity.getUsername(), userEntity.getTag());
            return EntityToModel.fromUserEntity(userEntity);
        } catch (Exception exception) {
            log.error("Failed to create user", exception);
            if (exception instanceof ThrowableGemGraphQLException) throw exception;
            else throw new ThrowableGemGraphQLException("Server encountered error while creating user");
        }
    }
}
