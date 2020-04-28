package io.github.firefang.mock.server.entity.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import io.github.firefang.power.web.group.CreateGroup;
import lombok.Data;

@Data
public class MockNodeForm {
    @NotNull(groups = CreateGroup.class)
    @Pattern(regexp = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}", groups = CreateGroup.class)
    private String ip;

    @Length(max = 255)
    private String remark;
}
