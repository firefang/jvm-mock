package io.github.firefang.mock.module.entity.dto;

import lombok.Data;

@Data
public class ResponseDTO {
    public static final int SOURCE = 1000;
    public static final int SUCCESS = 0;
    public static final int BUS_ERR = 100;
    private int code;
    private String msg;
    private Object data;
}
