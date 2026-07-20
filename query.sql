CREATE TABLE mock_server.scenario_master (
  id VARCHAR(20) PRIMARY KEY,
  description VARCHAR(50) NOT NULL,
  url_path VARCHAR(50) NOT NULL,
  url_method VARCHAR(10) NOT NULL DEFAULT 'POST',             
  active BOOLEAN NOT NULL DEFAULT true,                    
  created_by VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_by VARCHAR(50),
  updated_at TIMESTAMP,

  CONSTRAINT unique_scenario_path_method UNIQUE (url_path, url_method)
);

CREATE TABLE mock_server.scenario_detail (
  scenario_master_id VARCHAR(20) NOT NULL,
  priority INT NOT NULL DEFAULT 1,
  description VARCHAR(50) NOT NULL,
  condition_rule TEXT,
  response_delay_ms INT DEFAULT 0,
  response_http_status INT NOT NULL DEFAULT 200,                
	response_header TEXT NOT NULL DEFAULT '{}',          
  response_body TEXT NOT NULL DEFAULT '{}', 
  active BOOLEAN NOT NULL DEFAULT true,
  created_by VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_by VARCHAR(50),
  updated_at TIMESTAMP,

  CONSTRAINT pk_scenario_detail PRIMARY KEY (scenario_master_id, priority),
  CONSTRAINT fk_request_master FOREIGN KEY (scenario_master_id) REFERENCES mock_server.scenario_master(id)
);

-- sample
INSERT INTO mock_server.scenario_master (id, description, url_path, url_method, active, created_by, created_at, updated_by, updated_at) VALUES('SAMPLE', 'Ini sample', '/abc', 'POST', true, 'SYSTEM', '2026-07-19 13:48:14.130', NULL, NULL);

INSERT INTO mock_server.scenario_detail (scenario_master_id, priority, description, condition_rule, response_delay_ms, response_http_status, response_header, response_body, active, created_by, created_at, updated_by, updated_at) VALUES('SAMPLE', 1, 'SAMPLE_SCENARIO_ONE', 'headerValue("x-api-key") != null AND (headerValue("ada123") == ''123'' OR headerValue("ada123") == ''1234'') AND bodyValue("test321[''x-man''].abc") == ''123''', 0, 200, '{}', '{
  "uuid": "${uuid()}"
}', true, 'SYSTEM', '2026-07-20 15:53:18.419', NULL, NULL);
INSERT INTO mock_server.scenario_detail (scenario_master_id, priority, description, condition_rule, response_delay_ms, response_http_status, response_header, response_body, active, created_by, created_at, updated_by, updated_at) VALUES('SAMPLE', 2, 'SAMPLE_SCENARIO_TWO', NULL, 0, 200, '{
  "test321": "${(requestHeader[''x-api-key''])!""}"
  <#if (requestHeader[''x-api-key''])??>
  ,"test432" : "${requestHeader[''x-api-key'']}"
  </#if>
}', '{
  "test321": "${(requestHeader[''x-api-key''])!""}"
  <#if (requestHeader[''x-api-key''])??>
  ,"test432" : "${requestHeader[''x-api-key'']}"
  ,"concat": "${concat(requestHeader[''x-api-key''], requestHeader[''x-api-key''])}"
  </#if>
  ,"date": "${formatTime(''yyyy-MM-dd HH:mm:ss'')}"
  ,"body1": "${(requestBody.test321[''x-man''][''x-api-key''])!"null"}"
  ,"body2": "${(requestBody.test321[''x-man''].abc)!"null"}"
  ,"body3": "${(requestBody.test32.asli.palsu)!"null"}"
  ,"body4": "${(requestBody.test321.asli[''p-man''])!"null"}"
}', true, 'SYSTEM', '2026-07-20 15:53:18.419', NULL, NULL);