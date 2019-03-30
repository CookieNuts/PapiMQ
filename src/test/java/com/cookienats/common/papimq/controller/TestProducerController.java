package com.cookienats.common.papimq.controller;

import com.cookienats.common.papimq.common.utils.HttpProtoUtil;
import com.cookienats.common.papimq.common.utils.KryoUtil;
import com.cookienats.common.papimq.protos.common.Enums;
import com.cookienats.common.papimq.protos.msg.producer.EnqueMessage;
import com.google.protobuf.ByteString;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestProducerController {

    @Test
    public void TestEnqueMessage() throws IOException {
        List<String> messageList = new ArrayList<>();
        messageList.add("tttt1");
        messageList.add("t2222");
        EnqueMessage.EnqueMessageRequest request = EnqueMessage.EnqueMessageRequest.newBuilder()
                .setTopicName("topic-test1")
                .setMessage(ByteString.copyFrom(KryoUtil.writeObjectToByteArray(messageList)))
                .build();

        EnqueMessage.EnqueMessageResponse response = HttpProtoUtil.post(Enums.MsgId.ENQUE_MESSAGE_VALUE, request, EnqueMessage.EnqueMessageResponse.parser());
        System.out.println(response.toString());
    }

}
