package com.bluegems.server.gembackend.utils;

import com.bluegems.server.gembackend.exception.graphql.ThrowableGemGraphQLException;

import java.util.List;
import java.util.Objects;

public class ValidationUtils {

    public static Boolean areNonEmptyNorNull(List<String> strings) {
        boolean hasEmptyOrNull = strings.stream().anyMatch(string -> {
            return string==null || string.equalsIgnoreCase("");
        });
        if (hasEmptyOrNull) {
            throw new ThrowableGemGraphQLException("Invalid input parameters");
        }
        return true;
    }
}
