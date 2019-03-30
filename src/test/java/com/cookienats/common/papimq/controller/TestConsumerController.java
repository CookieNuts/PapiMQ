package com.cookienats.common.papimq.controller;

import com.cookienats.common.papimq.common.utils.HttpProtoUtil;
import com.cookienats.common.papimq.common.utils.KryoUtil;
import com.cookienats.common.papimq.protos.common.Enums;
import com.cookienats.common.papimq.protos.msg.consumer.FetchMessage;
import com.cookienats.common.papimq.protos.msg.consumer.ReportConsumeResult;
import com.cookienats.common.papimq.protos.msg.producer.EnqueMessage;
import com.google.protobuf.ByteString;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestConsumerController {

    @Test
    public void TestFetchMessage() throws IOException {
        FetchMessage.FetchMessageRequest request = FetchMessage.FetchMessageRequest.newBuilder()
                .setTopicName("topic-test1")
                .setSubscriberName("subscriber-test1")
                .build();

        FetchMessage.FetchMessageResponse response = HttpProtoUtil.post(Enums.MsgId.FETCH_MESSAGE_VALUE, request, FetchMessage.FetchMessageResponse.parser());
        System.out.println(response.toString());
    }

    @Test
    public void TestReportConsume() throws IOException {
        long requestId = 487650671777021952L;
        ReportConsumeResult.ReportConsumeResultRequest request = ReportConsumeResult.ReportConsumeResultRequest.newBuilder()
                .setTopicName("topic-test1")
                .setRequestId(requestId)
                .setSubscriberName("subscriber-test1")
                .setConsumeResult(Enums.ConsumeResult.CONSUME_SUCCESS)
                .build();

        ReportConsumeResult.ReportConsumeResultResponse response = HttpProtoUtil.post(Enums.MsgId.REPORT_COMSUME_VALUE, request, ReportConsumeResult.ReportConsumeResultResponse.parser());
        System.out.println("test result:" + response.toString());
    }

}
