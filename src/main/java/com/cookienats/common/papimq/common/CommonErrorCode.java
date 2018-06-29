package com.cookienats.common.papimq.common;

public enum CommonErrorCode {
    ENQUE_MESSAGE_ERROR("ENQ10001", "Enque Message ERROR!"),
    DEQUE_MESSAGE_ERROR("DEQ20001", "Deque Message ERROR!"),
    SERVICE_UNSUBSCRIBER_ERROR("SEV30001", "Service Unsubscriber Topic ERROR!"),
    SERVICE_MOVE_OFFSET_ERROR("SEV30002", "Service Move Consumer Offset ERROR!"),
    SERVICE_ADD_TOPIC_ERROR("SEV30003", "Service Add Topic ERROR!"),
    SERVICE_ADD_SUBSCRIBER_ERROR("SEV30004", "Service Add Subscriber ERROR!"),
    SERVICE_DEL_TOPIC_ERROR("SEV30005", "Service Del Topic ERROR!"),
    SERVICE_DEL_SUBSCRIBER_ERROR("SEV30006", "Service Del Subscriber ERROR!"),
    DSTREAM_REPORT_FAIL_ERROR("DSTM400001", "DownStream Report Consume Message Result ERROR!");


    String value;
    String desc;

    CommonErrorCode(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "[" +this.value +"] " + this.desc;
    }
}
