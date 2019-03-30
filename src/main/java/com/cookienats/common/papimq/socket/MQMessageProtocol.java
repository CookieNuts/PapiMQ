package com.cookienats.common.papimq.socket;

import com.cookienats.common.papimq.common.CommonConstant;

/**
 * MQ消息传输协议
 * 1.开始符号(0XAAAA，int 四字节)
 * 2.协议ID(MsgId, short 两字节)
 * 3.消息体长度(int)
 * 4.消息体字节流(Protobuf)
 * +-----------+-----------+--------------+--------+
 * + 开始符(4) | 协议ID(2) | 消息体长度(4) | 消息体 +
 * +-----------+-----------+--------------+--------+
 */
public class MQMessageProtocol {

    /**
     * 消息的协议起始标识
     */
    private int headData = CommonConstant.MESSAGE_PROTOCOL_HEADER;

    /**
     * 协议ID
     */
    private short msgId;

    /**
     * 消息主体长度
     */
    private int contentLength;

    /**
     * 消息主体二进制流
     */
    private byte[] content;

    public MQMessageProtocol(short msgId, int contentLength, byte[] content) {
        this.msgId = msgId;
        this.contentLength = contentLength;
        this.content = content;
    }

    public int getHeadData() {

        return headData;
    }

    public void setHeadData(int headData) {
        this.headData = headData;
    }

    public short getMsgId() {
        return msgId;
    }

    public void setMsgId(short msgId) {
        this.msgId = msgId;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
