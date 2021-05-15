package com.bluegems.server.gembackend.graphql.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Post {
    Long id;
    String description;
    String image;
    User user;
}
