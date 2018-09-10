package com.cookienats.common.papimq.controller;

import com.cookienats.common.papimq.common.CommonConstant;
import com.cookienats.common.papimq.common.CommonException;
import com.cookienats.common.papimq.common.utils.IdGenerator;
import com.cookienats.common.papimq.common.utils.RedisLockUtil;
import com.cookienats.common.papimq.entity.MessageEntity;
import com.cookienats.common.papimq.protos.common.Enums;
import com.cookienats.common.papimq.protos.common.Result;
import com.cookienats.common.papimq.protos.msg.consumer.FetchMessage;
import com.cookienats.common.papimq.protos.msg.consumer.ReportConsumeResult;
import com.cookienats.common.papimq.service.IDownStreamService;
import com.google.protobuf.ByteString;
import com.google.protobuf.GeneratedMessageV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer")
public class ComsumerController extends BaseController {

    @Autowired
    private IDownStreamService downStreamService;
    @Autowired
    private IdGenerator idGenerator;

    @RequestMapping(value={"/fetch-message","/msg1"})
    @ResponseBody
    public GeneratedMessageV3 fetchMessage(FetchMessage.FetchMessageRequest request, FetchMessage.FetchMessageResponse.Builder builder){
        String topicName = request.getTopicName();
        String subscriberName = request.getSubscriberName();
        long requestId = idGenerator.nextId();
        try {
            super.cacheRequestId(String.valueOf(requestId));
            MessageEntity messageEntity = downStreamService.fetchMessage(topicName, subscriberName);
            if(messageEntity == null){
                builder.setResult(Result.CommonResult.MESSAGE_EOF);
            }else {
                builder.setResult(Result.CommonResult.OK);
                builder.setMessage(ByteString.copyFrom(messageEntity.getMessageBody()));
            }
        }catch (CommonException ce){
            logger.error("FetchMessage InnerExecute Error! TopicName:[{}] Subscriber:[{}]", topicName, subscriberName, ce);
            builder.setResult(Result.CommonResult.SYSTEM_ERROR);
        }catch (Exception e){
            logger.error("FetchMessage Execute Error! TopicName:[{}] Subscriber:[{}]", topicName, subscriberName, e);
            builder.setResult(Result.CommonResult.SYSTEM_ERROR);
        }

        builder.setRequestId(requestId);
        return builder.build();
    }

    @RequestMapping(value={"/report-consume","/msg2"})
    @ResponseBody
    public GeneratedMessageV3 reportConsume(ReportConsumeResult.ReportConsumeResultRequest request, ReportConsumeResult.ReportConsumeResultResponse.Builder builder){
        String topicName = request.getTopicName();
        String subscriberName = request.getSubscriberName();
        long requestId = request.getRequestId();
        Enums.ConsumeResult consumeResult = request.getConsumeResult();

        if(super.checkRequestIdExist(String.valueOf(requestId))){
            if(RedisLockUtil.lock(CommonConstant.SUBSCRIBER_LOCK_KEY, subscriberName)) {
                try {
                    this.downStreamService.reportConsumeResult(topicName, subscriberName, consumeResult);
                    builder.setResult(Result.CommonResult.OK);
                }catch(CommonException ce){
                    logger.error("ReportConsumeResult InnerExecute Error! TopicName:[{}] SubscriberName:[{}]", topicName, subscriberName, ce);
                    builder.setResult(Result.CommonResult.SYSTEM_ERROR);
                }catch (Exception e){
                    logger.error("ReportConsumeResult Execute Error! TopicName:[{}] SubscriberName:[{}]", topicName, subscriberName, e);
                    builder.setResult(Result.CommonResult.SYSTEM_ERROR);
                }finally {
                    RedisLockUtil.unlock(CommonConstant.SUBSCRIBER_LOCK_KEY, subscriberName);
                    super.delRequestId(String.valueOf(requestId));
                }
            }else{
                logger.error("ReportConsumeResult Fetch LockKey Failed! SubscriberName:[{}]", subscriberName);
                builder.setResult(Result.CommonResult.CONNECT_TIME_OUT);
            }
        }else{
            logger.warn("ReportConsumeResult requestId:[{}] NOT Exist! Please FetchMessage First!", requestId);
            builder.setResult(Result.CommonResult.SYSTEM_ERROR);
        }

        return builder.build();
    }
}
