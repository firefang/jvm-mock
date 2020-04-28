package io.github.firefang.mock.server.entity.form;

import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class MockNodeForm {
    @Pattern(regexp = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}", groups = ICreateGroup.class)
    private String ip;
    private String remark;

    public interface ICreateGroup {
    }
}
