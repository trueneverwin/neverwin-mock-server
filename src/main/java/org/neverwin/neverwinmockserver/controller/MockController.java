package org.neverwin.neverwinmockserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.neverwin.neverwinmockserver.service.CacheService;
import org.neverwin.neverwinmockserver.service.MockService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MockController {

    private final MockService mockService;
    private final CacheService cacheService;

    @RequestMapping(value = "/**")
    public ResponseEntity<Object> handleMockRequest(HttpServletRequest request) {
        return mockService.execute(request);
    }

    @GetMapping(value = "admin/clear-cache")
    public ResponseEntity<Object> setting() {
        cacheService.clearAllCache();
        return new ResponseEntity<>("CACHE CLEAR", HttpStatus.OK);
    }

}
