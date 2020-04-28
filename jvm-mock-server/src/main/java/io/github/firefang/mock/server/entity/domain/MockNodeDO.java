package io.github.firefang.mock.server.entity.domain;

import lombok.Data;

@Data
public class MockNodeDO {
    private Integer id;
    private String ip;
    private Boolean online;
    private String remark;
}
