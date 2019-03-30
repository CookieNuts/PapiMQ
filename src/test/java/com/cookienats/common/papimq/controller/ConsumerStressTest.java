package com.cookienats.common.papimq.controller;

import com.cookienats.common.papimq.common.utils.HttpProtoUtil;
import com.cookienats.common.papimq.protos.common.Enums;
import com.cookienats.common.papimq.protos.common.Result;
import com.cookienats.common.papimq.protos.msg.consumer.FetchMessage;
import com.cookienats.common.papimq.protos.msg.consumer.ReportConsumeResult;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsumerStressTest {
    private static final Integer CONSUMER_THREADCOUNT = 50;
    private static AtomicBoolean STARTUP = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService consumerThreadPool = Executors.newFixedThreadPool(CONSUMER_THREADCOUNT);

        while (STARTUP.get()){
            for (int i = 0; i < CONSUMER_THREADCOUNT; i++){
                consumerThreadPool.submit(new ConsumerTask(i + 1));
            }
            Thread.sleep(200);
        }
    }


    static class ConsumerTask implements Callable<ExecuteResult> {
        private Integer subscriberIndex;

        public ConsumerTask() {}

        public ConsumerTask(Integer subscriberIndex) {
            this.subscriberIndex = subscriberIndex;
        }

        @Override
        public ExecuteResult call() throws Exception {
            ExecuteResult result = new ExecuteResult();
            FetchMessage.FetchMessageRequest fetchRequest = FetchMessage.FetchMessageRequest.newBuilder()
                    .setTopicName("topic-test1")
                    .setSubscriberName("subscriber-test" + subscriberIndex)
                    .build();

            FetchMessage.FetchMessageResponse fetchResponse = HttpProtoUtil.post(Enums.MsgId.FETCH_MESSAGE_VALUE, fetchRequest, FetchMessage.FetchMessageResponse.parser());

            System.out.println("TTTTTT:" + fetchResponse.toString());
            ReportConsumeResult.ReportConsumeResultResponse reportResponse = null;
            if(fetchResponse !=null && fetchResponse.getResult() == Result.CommonResult.OK){
                //模拟处理消息，等待1秒
                Thread.sleep(1000);
                ReportConsumeResult.ReportConsumeResultRequest reportRequest = ReportConsumeResult.ReportConsumeResultRequest.newBuilder()
                        .setTopicName("topic-test1")
                        .setRequestId(fetchResponse.getRequestId())
                        .setSubscriberName("subscriber-test" + subscriberIndex)
                        .setConsumeResult(Enums.ConsumeResult.CONSUME_SUCCESS)
                        .build();

                reportResponse = HttpProtoUtil.post(Enums.MsgId.REPORT_COMSUME_VALUE, reportRequest, ReportConsumeResult.ReportConsumeResultResponse.parser());
                if(reportResponse != null && reportResponse.getResult() == Result.CommonResult.OK){
                    result.setCode(1);
                    result.setMsg("OK");
                }else{
                    result.setCode(0);
                    result.setMsg(Thread.currentThread().getName()+ " report Message Error!");
                }
            }else{
                result.setCode(0);
                result.setMsg(Thread.currentThread().getName()+ " fetch Message Error!");
            }

            return result;
        }
    }

}
