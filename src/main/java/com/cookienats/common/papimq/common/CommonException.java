package com.cookienats.common.papimq.common;

public class CommonException extends Exception{
    String extendDesc;

    public String getExtendDesc() {
        return extendDesc;
    }

    public void setExtendDesc(String extendDesc) {
        this.extendDesc = extendDesc;
    }

    public CommonException(Object obj,String extendDesc) {
        super(obj.toString() + "ExtendDesc: ["+ extendDesc +"]");
    }
}
