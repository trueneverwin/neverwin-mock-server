package org.neverwin.neverwinmockserver.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.neverwin.neverwinmockserver.helper.template.TemplateEngineHelper;
import org.neverwin.neverwinmockserver.model.ScenarioDetail;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseBuilderHelper {

    private final TemplateEngineHelper templateEngineHelper;
    private final JsonHelper jsonHelper;

    public ResponseEntity<Object> buildResponse(ScenarioDetail scenarioDetail, Map<String, String> headers, Map<String, String> queryParams, String body) {
        Map<String, Object> requestContext = buildRequestContext(headers, queryParams, body);

        String templateName = scenarioDetail.getId().getScenarioMasterId() + "#" + scenarioDetail.getId().getPriority();

        CompletableFuture<HttpHeaders> headersFuture = CompletableFuture.supplyAsync(() -> buildHeaders(templateName, scenarioDetail.getResponseHeader(), requestContext));
        CompletableFuture<Object> bodyFuture = CompletableFuture.supplyAsync(() -> buildBody(templateName, scenarioDetail.getResponseBody(), requestContext));

        CompletableFuture.allOf(headersFuture, bodyFuture).join();

        HttpHeaders responseHeaders = headersFuture.join();
        Object responseBody = bodyFuture.join();

        applyDelay(scenarioDetail.getDelayResponseMs());

        return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.valueOf(scenarioDetail.getHttpStatus()));
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

    private HttpHeaders buildHeaders(String templateName, String headerTemplate, Map<String, Object> context) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("powered-by", "Neverwin");

        String renderedHeader = templateEngineHelper.render(templateName, headerTemplate, context);
        if (renderedHeader != null) {
            Map<String, Object> headerMap = jsonHelper.toMap(renderedHeader);
            if (headerMap != null) {
                headerMap.forEach((k, v) -> httpHeaders.add(k, v.toString()));
            }
        }
        return httpHeaders;
    }

    private Object buildBody(String templateName, String bodyTemplate, Map<String, Object> context) {
        String renderedBody = templateEngineHelper.render(templateName, bodyTemplate, context);
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
