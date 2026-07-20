package org.neverwin.mockserver.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.neverwin.mockserver.helper.ConditionEvaluatorHelper;
import org.neverwin.mockserver.helper.ResponseBuilderHelper;
import org.neverwin.mockserver.model.ScenarioMaster;
import org.neverwin.mockserver.model.ScenarioRequest;
import org.neverwin.mockserver.repository.MockResponseRepository;
import org.neverwin.mockserver.repository.ScenarioMasterRepository;
import org.neverwin.mockserver.repository.ScenarioRequestRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MockService {

    private final ScenarioMasterRepository masterRepository;
    private final ScenarioRequestRepository requestRepository;
    private final MockResponseRepository responseRepository;

    private final ConditionEvaluatorHelper conditionEvaluator;
    private final ResponseBuilderHelper responseBuilder;

    public ResponseEntity<Object> execute(HttpServletRequest request) {
        String path = request.getRequestURI();

        Map<String, String> headers = extractHeaders(request);
        Map<String, String> queryParams = extractQueryParams(request);
        String body = extractBody(request);

        ScenarioMaster master = masterRepository.findByUrlPathAndUrlMethodAndActiveTrue(path, request.getMethod().toUpperCase());
        if (master == null) return errorMessage("No scenario master found for this path and method");

        List<ScenarioRequest> requests = requestRepository.findByIdScenarioMasterIdAndActiveTrueOrderByIdPriorityAsc(master.getId());
        if (requests == null || requests.isEmpty()) return errorMessage("No scenario request found for this path and method");

        ScenarioRequest matchedRequest = conditionEvaluator.findMatchedRule(requests, headers, queryParams, body);
        if (matchedRequest == null) return errorMessage("No scenario request found for this condition");

        return responseRepository.findById(matchedRequest.getMockResponseId())
                .map(mockResponse -> responseBuilder.buildResponse(mockResponse, headers, queryParams, body, matchedRequest.getDelayResponseMs()))
                .orElseGet(() -> errorMessage("No mock response found for this condition"));
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
