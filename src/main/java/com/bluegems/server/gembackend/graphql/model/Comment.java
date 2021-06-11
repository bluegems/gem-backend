package com.bluegems.server.gembackend.graphql.model;

import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Builder
@Value
public class Comment {
    Long id;
    String text;
    ZonedDateTime datetime;
    User user;
    Post post;
}
