package com.cookienats.common.papimq.controller;

import com.cookienats.common.papimq.common.utils.HttpProtoUtil;
import com.cookienats.common.papimq.common.utils.KryoUtil;
import com.cookienats.common.papimq.protos.common.Enums;
import com.cookienats.common.papimq.protos.common.Result;
import com.cookienats.common.papimq.protos.msg.producer.EnqueMessage;
import com.google.protobuf.ByteString;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProduceStressTest{
    private static final Integer PRODUCE_THREADCOUNT = 2;
    private static AtomicBoolean STARTUP = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService produceThreadPool = Executors.newFixedThreadPool(PRODUCE_THREADCOUNT);
        while (STARTUP.get()) {
            for (int i = 0; i < PRODUCE_THREADCOUNT; i++) {
                produceThreadPool.submit(new ProduceTask());
            }
            Thread.sleep(3000);
        }
    }


    static class ProduceTask implements Callable<ExecuteResult> {
        @Override
        public ExecuteResult call() throws Exception {
            ExecuteResult result = new ExecuteResult();

            List<String> messageList = new ArrayList<>();
            messageList.add("test-message1" + Thread.currentThread().getName());
            messageList.add("test-message2" + Thread.currentThread().getName());
            EnqueMessage.EnqueMessageRequest request = EnqueMessage.EnqueMessageRequest.newBuilder()
                    .setTopicName("topic-test1")
                    .setMessage(ByteString.copyFrom(KryoUtil.writeObjectToByteArray(messageList)))
                    .build();

            EnqueMessage.EnqueMessageResponse response = HttpProtoUtil.post(Enums.MsgId.ENQUE_MESSAGE_VALUE, request, EnqueMessage.EnqueMessageResponse.parser());

            if (response.getResult() == Result.CommonResult.OK) {
                result.setCode(1);
                result.setMsg("OK");
            } else {
                result.setCode(0);
                result.setMsg(Thread.currentThread().getName() + "message Enque ERROR!");
            }

            return result;
        }
    }
}
