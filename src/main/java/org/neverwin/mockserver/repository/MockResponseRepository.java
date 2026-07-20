package org.neverwin.mockserver.repository;

import org.jspecify.annotations.NonNull;
import org.neverwin.mockserver.model.MockResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MockResponseRepository extends JpaRepository<MockResponse, String> {

    @Override
    @Cacheable(value = "mockResponse", key = "#id")
    Optional<MockResponse> findById(@NonNull String id);

}
