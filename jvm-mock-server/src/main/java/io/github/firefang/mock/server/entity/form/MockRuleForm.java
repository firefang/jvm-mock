package io.github.firefang.mock.server.entity.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class MockRuleForm {
    @NotBlank
    private String classPattern;
    @NotBlank
    private String methodPattern;
    @NotBlank
    private String script;
}
