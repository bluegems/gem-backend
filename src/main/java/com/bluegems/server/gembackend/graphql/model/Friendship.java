package com.bluegems.server.gembackend.graphql.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Friendship {
    Boolean exists;
    User currentUser;
    User otherUser;
    String status;
    User modifiedBy;
}
