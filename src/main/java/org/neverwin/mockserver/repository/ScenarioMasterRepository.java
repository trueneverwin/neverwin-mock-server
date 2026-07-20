package org.neverwin.mockserver.repository;

import org.neverwin.mockserver.model.ScenarioMaster;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScenarioMasterRepository extends JpaRepository<ScenarioMaster, String> {

    @Cacheable(value = "scenarioMaster", key = "#urlPath + '-' + #urlMethod")
    ScenarioMaster findByUrlPathAndUrlMethodAndActiveTrue(String urlPath, String urlMethod);
}
