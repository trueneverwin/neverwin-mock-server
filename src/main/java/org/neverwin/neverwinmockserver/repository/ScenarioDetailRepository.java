package org.neverwin.neverwinmockserver.repository;

import org.neverwin.neverwinmockserver.model.ScenarioDetail;
import org.neverwin.neverwinmockserver.model.ScenarioDetailId;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScenarioDetailRepository extends JpaRepository<ScenarioDetail, ScenarioDetailId> {

    @Cacheable(value = "scenarioDetails", key = "#scenarioMasterId")
    List<ScenarioDetail> findByIdScenarioMasterIdAndActiveTrueOrderByIdPriorityAsc(String scenarioMasterId);
}
