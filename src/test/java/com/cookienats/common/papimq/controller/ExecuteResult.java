package com.cookienats.common.papimq.controller;

public class ExecuteResult {
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ExecuteResult{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
