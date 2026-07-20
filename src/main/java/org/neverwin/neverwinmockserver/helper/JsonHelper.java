package org.neverwin.neverwinmockserver.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JsonHelper {
    private final ObjectMapper objectMapper;

    public boolean isValidJson(String jsonString) {
        if (jsonString == null || jsonString.trim().isEmpty()) return false;
        try {
            JsonNode node = objectMapper.readTree(jsonString);
            return node.isObject() || node.isArray();
        } catch (JacksonException e) {
            return false;
        }
    }

    public Map<String, Object> toMap(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<>() {});
        } catch (Exception e) {
            return null;
        }
    }
}
