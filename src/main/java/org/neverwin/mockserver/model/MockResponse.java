package org.neverwin.mockserver.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "mock_response", schema = "mock_server")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockResponse extends BaseModel {

    @Id
    @Column(name = "id", length = 20)
    private String id;

    @Column(name = "description", length = 25, nullable = false)
    private String description;

    @Column(name = "http_status", nullable = false)
    private Integer httpStatus = 200;

    @Column(name = "response_header", columnDefinition = "TEXT", nullable = false)
    private String responseHeader = "{}";

    @Column(name = "response_body", columnDefinition = "TEXT", nullable = false)
    private String responseBody = "{}";

}
