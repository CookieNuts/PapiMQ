package com.cookienats.common.papimq.common;

public enum CommonResult {
    CONSUME_FAIL(0),
    CONSUME_SUCCESS(1);

    private Integer code;

    CommonResult(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
