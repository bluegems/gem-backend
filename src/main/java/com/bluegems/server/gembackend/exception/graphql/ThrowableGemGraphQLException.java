package com.bluegems.server.gembackend.exception.graphql;

import lombok.Getter;

@Getter
public class ThrowableGemGraphQLException extends RuntimeException {

    private GemGraphQLErrorExtensions extensions;

    public ThrowableGemGraphQLException(String message, GemGraphQLErrorExtensions extensions) {
        super(message);
        this.extensions = extensions;
    }

    public ThrowableGemGraphQLException(String message) {
        super(message);
    }
}
