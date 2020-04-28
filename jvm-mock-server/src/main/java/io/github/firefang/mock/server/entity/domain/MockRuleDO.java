package io.github.firefang.mock.server.entity.domain;

import lombok.Data;

@Data
public class MockRuleDO {
    private String classPattern;
    private String methodPattern;
    private String script;
}
