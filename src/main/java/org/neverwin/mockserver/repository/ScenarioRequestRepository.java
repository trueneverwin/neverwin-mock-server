package org.neverwin.mockserver.repository;

import org.neverwin.mockserver.model.ScenarioRequest;
import org.neverwin.mockserver.model.ScenarioRequestId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioRequestRepository extends JpaRepository<ScenarioRequest, ScenarioRequestId> {

    @Cacheable(value = "scenarioRequests", key = "#scenarioMasterId")
    List<ScenarioRequest> findByIdScenarioMasterIdAndActiveTrueOrderByIdPriorityAsc(String scenarioMasterId);
}
