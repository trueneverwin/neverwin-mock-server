package org.neverwin.mockserver.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neverwin.mockserver.helper.template.TemplateEngineHelper;
import org.neverwin.mockserver.model.MockResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseBuilderHelper {

    private final TemplateEngineHelper templateEngineHelper;
    private final JsonHelper jsonHelper;

    public ResponseEntity<Object> buildResponse(MockResponse mockResponse, Map<String, String> headers, Map<String, String> queryParams, String body, int delayInMs) {
        Map<String, Object> requestContext = buildRequestContext(headers, queryParams, body);

        HttpHeaders responseHeaders = buildHeaders(mockResponse.getResponseHeader(), requestContext);
        Object responseBody = buildBody(mockResponse.getResponseBody(), requestContext);

        applyDelay(delayInMs);

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.valueOf(mockResponse.getHttpStatus()));
    }

    private Map<String, Object> buildRequestContext(Map<String, String> headers, Map<String, String> queryParams, String body) {
        Map<String, Object> map = new HashMap<>();
        if (headers != null && !headers.isEmpty()) map.put("requestHeader", headers);
        if (queryParams != null && !queryParams.isEmpty()) map.put("requestParam", queryParams);

        if (body != null && !body.isEmpty()) {
            map.put("requestBody", jsonHelper.isValidJson(body) ? Objects.requireNonNullElse(jsonHelper.toMap(body), body) : body);
        }
        return map;
    }

    private HttpHeaders buildHeaders(String headerTemplate, Map<String, Object> context) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("powered-by", "Neverwin");

        String renderedHeader = templateEngineHelper.render(headerTemplate, context);
        log.info("renderHeader => {} : {}", headerTemplate, context);
        log.info("hasil => {}", renderedHeader);
        if (renderedHeader != null) {
            Map<String, Object> headerMap = jsonHelper.toMap(renderedHeader);
            if (headerMap != null) {
                headerMap.forEach((k, v) -> httpHeaders.add(k, v.toString()));
            }
        }
        return httpHeaders;
    }

    private Object buildBody(String bodyTemplate, Map<String, Object> context) {
        String renderedBody = templateEngineHelper.render(bodyTemplate, context);
        if (renderedBody != null && jsonHelper.isValidJson(renderedBody)) {
            return Objects.requireNonNullElse(jsonHelper.toMap(renderedBody), renderedBody);
        }
        return renderedBody != null ? renderedBody : "";
    }

    private void applyDelay(int delayInMs) {
        if (delayInMs > 0) {
            try {
                if (delayInMs > 17) delayInMs = delayInMs - 17;
                Thread.sleep(delayInMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
