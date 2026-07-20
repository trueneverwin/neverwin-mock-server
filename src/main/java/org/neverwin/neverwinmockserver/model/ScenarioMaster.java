package org.neverwin.neverwinmockserver.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "scenario_master", schema = "mock_server")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScenarioMaster extends BaseModel {

    @Id
    @Column(name = "id", length = 20)
    private String id;

    @Column(name = "description", length = 50, nullable = false)
    private String description;

    @Column(name = "url_path", length = 50, nullable = false, unique = true)
    private String urlPath;

    @Column(name = "url_method", nullable = false)
    private String urlMethod = "POST";

    @Column(name = "active", nullable = false)
    private Boolean active = true;

}
