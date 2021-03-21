package com.bluegems.server.gembackend.graphql.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class User {
    String tag;
    String username;
    String firstName;
}
