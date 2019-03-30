package com.cookienats.common.papimq.socket;

import com.cookienats.common.papimq.common.CommonConstant;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.Buffer;
import java.util.List;

/**
 * 1.开始符号(0XAAAA，int 四字节)
 * 2.协议ID(MsgId, short 两字节)
 * 3.消息体长度(int)
 * 4.消息体字节流(Protobuf)
 * +-----------+-----------+--------------+--------+
 * + 开始符(4) | 协议ID(2) | 消息体长度(4) | 消息体 +
 * +-----------+-----------+--------------+--------+
 */
public class MQMessageDecoder  extends ByteToMessageDecoder {

    //消息起始标识(int) + 协议ID(short) + 消息长度(int)
    public final int BASE_LENGTH = 4 + 2 + 4;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() >= BASE_LENGTH){
            int beginReader;

            while (true){
                beginReader = byteBuf.readerIndex();
                //标记包头开始的index
                byteBuf.markReaderIndex();
                if(byteBuf.readInt() == CommonConstant.MESSAGE_PROTOCOL_HEADER){
                    break;
                }

                //未读到包头，略过一个字节
                //每次略过一个字节，重新标记包头开始
                byteBuf.resetReaderIndex();
                byteBuf.readByte();

                //当数据包长度不满足BASE_LENGTH时，则跳过此包信息
                if(byteBuf.readableBytes() < BASE_LENGTH){
                    return;
                }
            }

            int msgId = byteBuf.readUnsignedShort();
            if(!MsgIdProtoParser.getInstance().containsMsgId(msgId)){
                //无效协议ID
                byteBuf.readerIndex(beginReader);
                return;
            }

            int contentLength = byteBuf.readInt();
            if(byteBuf.readableBytes() < contentLength){
                // 还原读指针
                byteBuf.readerIndex(beginReader);
                return;
            }

            byte[] data = new byte[contentLength];
            byteBuf.readBytes(data);

            MQMessageProtocol protocol = new MQMessageProtocol((short)msgId, data.length, data);
            list.add(protocol);
        }
    }

}
