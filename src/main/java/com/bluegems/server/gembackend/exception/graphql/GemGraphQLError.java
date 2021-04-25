package com.bluegems.server.gembackend.exception.graphql;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor
public class GemGraphQLError implements GraphQLError {

    private final String message;
    private final GemGraphQLErrorExtensions extensions;

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return null;
    }

    @Override
    public List<Object> getPath() {
        return null;
    }

    @Override
    public Map<String, Object> toSpecification() {
        return null;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return (this.extensions!=null) ? this.extensions.getMap() : null;
    }
}
