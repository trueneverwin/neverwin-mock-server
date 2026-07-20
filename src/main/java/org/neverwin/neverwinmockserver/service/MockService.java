package org.neverwin.neverwinmockserver.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.neverwin.neverwinmockserver.helper.ConditionEvaluatorHelper;
import org.neverwin.neverwinmockserver.helper.ResponseBuilderHelper;
import org.neverwin.neverwinmockserver.model.ScenarioMaster;
import org.neverwin.neverwinmockserver.model.ScenarioDetail;
import org.neverwin.neverwinmockserver.repository.ScenarioMasterRepository;
import org.neverwin.neverwinmockserver.repository.ScenarioDetailRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MockService {

    private final ScenarioMasterRepository masterRepository;
    private final ScenarioDetailRepository requestRepository;

    private final ConditionEvaluatorHelper conditionEvaluator;
    private final ResponseBuilderHelper responseBuilder;

    public ResponseEntity<Object> execute(HttpServletRequest request) {
        String path = request.getRequestURI();

        CompletableFuture<Map<String, String>> headersFuture = CompletableFuture.supplyAsync(() -> extractHeaders(request));
        CompletableFuture<Map<String, String>> queryParamsFuture = CompletableFuture.supplyAsync(() -> extractQueryParams(request));
        CompletableFuture<String> bodyFuture = CompletableFuture.supplyAsync(() -> extractBody(request));

        CompletableFuture.allOf(headersFuture, queryParamsFuture, bodyFuture).join();

        Map<String, String> headers = headersFuture.join();
        Map<String, String> queryParams = queryParamsFuture.join();
        String body = bodyFuture.join();

        ScenarioMaster master = masterRepository.findByUrlPathAndUrlMethodAndActiveTrue(path, request.getMethod().toUpperCase());
        if (master == null) return errorMessage("No scenario master found for this path and method");

        List<ScenarioDetail> requests = requestRepository.findByIdScenarioMasterIdAndActiveTrueOrderByIdPriorityAsc(master.getId());
        if (requests == null || requests.isEmpty()) return errorMessage("No scenario request found for this path and method");

        ScenarioDetail matchedScenario = conditionEvaluator.findMatchedRule(requests, headers, queryParams, body);
        if (matchedScenario == null) return errorMessage("No scenario request found for this condition");

        return responseBuilder.buildResponse(matchedScenario, headers, queryParams, body);
    }

    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        request.getHeaderNames().asIterator().forEachRemaining(
                headerName -> headers.put(headerName.toLowerCase(), request.getHeader(headerName))
        );
        return headers;
    }

    private Map<String, String> extractQueryParams(HttpServletRequest request) {
        Map<String, String> queryParams = new HashMap<>();
        if (request.getParameterMap().isEmpty()) return queryParams;

        request.getParameterMap().forEach((key, values) -> {
            if (values != null && values.length > 0) queryParams.put(key, values[0]);
        });
        return queryParams;
    }

    private String extractBody(HttpServletRequest request) {
        try {
            return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Exception ignored) {
            return null;
        }
    }

    private ResponseEntity<Object> errorMessage(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

}
