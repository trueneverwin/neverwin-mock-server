package org.neverwin.mockserver.helper;

import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@AllArgsConstructor
@Slf4j
public class MockContextHelper {

    public final Map<String, String> headers;
    public final Map<String, String> queryParams;
    public final String body;

    public String headerValue(String key) {
        if (headers == null || headers.isEmpty()) return null;
        String value = headers.get(key);
        log.info("Header => {} : {}", key, value);
        return value;
    }

    public String queryValue(String key) {
        if (queryParams == null || queryParams.isEmpty()) return null;
        return queryParams.get(key);
    }

    public String bodyValue(String path) {
        if (body == null || body.isBlank()) return null;

        String jsonPath = path;
        if (!jsonPath.startsWith("$")) jsonPath = path.startsWith(".") ? "$" + path : "$." + path;

        try {
            Object result = JsonPath.read(body, jsonPath);
            log.info("Body => {} : {}", jsonPath, result);
            return result != null ? result.toString() : null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean hasJson(String path) {
        return bodyValue(path) != null;
    }

}
