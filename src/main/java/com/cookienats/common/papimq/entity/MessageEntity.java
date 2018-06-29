package com.cookienats.common.papimq.entity;

import java.io.Serializable;

public class MessageEntity implements Serializable {
    //每个分区消息hash自增id，hashKey
    private Long id;
    private byte[] messageBody;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(byte[] messageBody) {
        this.messageBody = messageBody;
    }
}
