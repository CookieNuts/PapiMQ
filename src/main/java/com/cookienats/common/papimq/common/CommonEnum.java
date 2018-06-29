package com.cookienats.common.papimq.common;

public class CommonEnum {

    public static enum RelationStatus{
        NORMAL(1),
        DELETE(0);

        private Integer code;

        RelationStatus(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }
    }
}
