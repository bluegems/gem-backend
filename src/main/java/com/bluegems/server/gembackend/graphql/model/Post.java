package com.bluegems.server.gembackend.graphql.model;

import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Builder
@Value
public class Post {
    Long id;
    String description;
    String image;
    User user;
    ZonedDateTime modifiedDatetime;
}
