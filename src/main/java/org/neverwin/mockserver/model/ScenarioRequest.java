package org.neverwin.mockserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "scenario_request", schema = "mock_server")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScenarioRequest extends BaseModel {

    @EmbeddedId
    private ScenarioRequestId id;

    @Column(name = "description", length = 50, nullable = false)
    private String description;

    @Column(name = "condition_rule", columnDefinition = "TEXT")
    private String conditionRule;

    @Column(name = "delay_response_ms")
    private Integer delayResponseMs = 0;

    @Column(name = "mock_response_id", length = 20, nullable = false)
    private String mockResponseId;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

}
