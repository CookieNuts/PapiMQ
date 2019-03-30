package com.cookienats.common.papimq.socket;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.GeneratedMessageV3.Builder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public class MQMessageHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger("socket");

    private static final String handleClassPkgPath = "com.cookienats.common.papimq.controller";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            MsgIdProtoParser parser = MsgIdProtoParser.getInstance();
            MQMessageProtocol protocol = (MQMessageProtocol) msg;
            short msgId = protocol.getMsgId();
            MsgIdProtoParser.MsgProtoWapper wapper = parser.getMsgProtoWapperByMsgId((int)msgId);
            if(wapper != null){
                return;
            }else{
                if(!StringUtils.isEmpty(wapper.getActionName())){
                    String executeClass = wapper.getActionName().split("$$")[0];
                    String executeMethod = wapper.getActionName().split("$$")[1];

                    Object clz = Class.forName(handleClassPkgPath+executeClass).newInstance();
                    //获取方法
                    Method method = clz.getClass().getDeclaredMethod(executeMethod, wapper.getRequestClass());
                    GeneratedMessageV3 request = (GeneratedMessageV3)wapper.getRequestParser().parseFrom(protocol.getContent());
                    Builder responseBuilder = (Builder<?>)wapper.getResponseClass().getDeclaredMethod("newBuilder").invoke(null);
                    GeneratedMessageV3 response = (GeneratedMessageV3)method.invoke(clz, request, responseBuilder);
                    byte[] responseByte = response.toByteArray();

                    MQMessageProtocol result = new MQMessageProtocol(msgId, responseByte.length, responseByte);
                    ctx.writeAndFlush(result);
                }
            }
        }catch (Exception e){
            logger.error(ctx.channel().remoteAddress() +"解析数据处理失败"+JSON.toJSONString(msg), e);
            e.printStackTrace();
            ctx.pipeline().fireExceptionCaught(e);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // cause.printStackTrace();
        ctx.close();
    }
}
