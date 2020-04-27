package io.github.firefang.mock.module.entity.domain;

import lombok.Data;

@Data
public class MockConfigDO {
    private String classPattern;
    private String methodPattern;
    private String script;
}
