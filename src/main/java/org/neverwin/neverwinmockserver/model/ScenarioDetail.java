package org.neverwin.neverwinmockserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "scenario_detail", schema = "mock_server")
@Data
@EqualsAndHashCode(callSuper = true)
public class ScenarioDetail extends BaseModel {

    @EmbeddedId
    private ScenarioDetailId id;

    @Column(name = "description", length = 50, nullable = false)
    private String description;

    @Column(name = "condition_rule", columnDefinition = "TEXT")
    private String conditionRule;

    @Column(name = "response_delay_ms")
    private Integer delayResponseMs = 0;

    @Column(name = "response_http_status", nullable = false)
    private Integer httpStatus = 200;

    @Column(name = "response_header", columnDefinition = "TEXT", nullable = false)
    private String responseHeader = "{}";

    @Column(name = "response_body", columnDefinition = "TEXT", nullable = false)
    private String responseBody = "{}";

    @Column(name = "active", nullable = false)
    private Boolean active = true;

}
