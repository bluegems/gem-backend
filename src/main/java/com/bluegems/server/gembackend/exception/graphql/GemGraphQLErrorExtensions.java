package com.bluegems.server.gembackend.exception.graphql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GemGraphQLErrorExtensions {

    private String invalidField;

    public Map<String, Object> getMap() {
        Map<String, Object> extensionsMap = new HashMap<>();
        if (invalidField!=null && !invalidField.isEmpty()) extensionsMap.put("invalidField", invalidField);
        return extensionsMap;
    }
}
