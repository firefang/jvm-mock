package io.github.firefang.mock.server.entity.domain;

import lombok.Data;

@Data
public class MockRuleDO {
    private Integer id;
    private Integer nodeId;
    private Integer watcherId;
    private String classPattern;
    private String methodPattern;
    private String script;
}
