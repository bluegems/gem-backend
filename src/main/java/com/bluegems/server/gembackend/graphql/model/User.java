package com.bluegems.server.gembackend.graphql.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Builder
@Value
public class User {
    String tag;
    String username;
    String firstName;
    String lastName;
    String bio;
    LocalDate birthdate;
    String profilePicture;
    Friendship friendship;
    List<User> friends;
    List<Post> posts;
}
