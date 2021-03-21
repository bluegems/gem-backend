package com.bluegems.server.gembackend.graphql.resolver;

import com.bluegems.server.gembackend.entity.UserEntity;
import com.bluegems.server.gembackend.graphql.model.User;
import com.bluegems.server.gembackend.repository.UserRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class UserResolver implements GraphQLQueryResolver {

    @Autowired
    UserRepository userRepository;

    public User user(String username, String tag) {
        log.info("Hit resolver for User");
        Optional<UserEntity> user = userRepository.fetchUserByUsernameAndTag(username, tag);
        if (user.isPresent()) {
            return User.builder()
                    .tag(user.get().getTag())
                    .username(user.get().getUsername())
                    .firstName(user.get().getFirstName())
                    .build();
        } else {
            return User.builder().tag(tag).username("FarisTone").firstName("Parinith").build();
        }
    }
}
