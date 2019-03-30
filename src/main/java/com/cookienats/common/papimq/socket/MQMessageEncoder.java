package com.cookienats.common.papimq.socket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 1.开始符号(0XAAAA，int 四字节)
 * 2.协议ID(MsgId, short 两字节)
 * 3.消息体长度(int)
 * 4.消息体字节流(Protobuf)
 * +-----------+-----------+--------------+--------+
 * + 开始符(4) | 协议ID(2) | 消息体长度(4) | 消息体 +
 * +-----------+-----------+--------------+--------+
 */
public class MQMessageEncoder extends MessageToByteEncoder<MQMessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MQMessageProtocol msg, ByteBuf byteBuf) throws Exception {
        //1.写入消息的开始标识(int类型)
        byteBuf.writeInt(msg.getHeadData());
        //2.写入协议ID(short类型)
        byteBuf.writeShort(msg.getMsgId());
        //3.写入消息主体的长度(int类型)
        byteBuf.writeInt(msg.getContentLength());
        //4.写入消息的内容(byte[]类型)
        byteBuf.writeBytes(msg.getContent());
    }
}
