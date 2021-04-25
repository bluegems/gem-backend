package com.bluegems.server.gembackend.exception.graphql;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public class GemGraphQLErrorExtensions {

    private final String invalidField;

    public Map<String, Object> getMap() {
        Map<String, Object> extensionsMap = new HashMap<>();
        if (invalidField!=null && !invalidField.isEmpty()) extensionsMap.put("invalidField", invalidField);
        return extensionsMap;
    }
}
