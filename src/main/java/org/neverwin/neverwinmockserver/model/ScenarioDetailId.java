package org.neverwin.neverwinmockserver.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioDetailId implements Serializable {
    private String scenarioMasterId;
    private Integer priority;
}