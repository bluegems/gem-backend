package com.bluegems.server.gembackend.graphql.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.util.Date;

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
}
