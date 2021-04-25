package com.bluegems.server.gembackend.graphql.model;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Builder
@Value
public class Account {
    UUID id;
    String email;
}
