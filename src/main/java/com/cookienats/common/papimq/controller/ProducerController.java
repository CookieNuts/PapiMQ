package com.cookienats.common.papimq.controller;

import com.cookienats.common.papimq.common.CommonConstant;
import com.cookienats.common.papimq.common.CommonException;
import com.cookienats.common.papimq.common.utils.IdGenerator;
import com.cookienats.common.papimq.common.utils.RedisLockUtil;
import com.cookienats.common.papimq.entity.MessageEntity;
import com.cookienats.common.papimq.protos.common.Result;
import com.cookienats.common.papimq.protos.msg.producer.EnqueMessage;
import com.cookienats.common.papimq.service.IUpStreamService;
import com.google.protobuf.GeneratedMessageV3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/producer")
public class ProducerController extends BaseController{
    @Autowired
    private IUpStreamService upStreamService;
    @Autowired
    private IdGenerator idGenerator;

    @RequestMapping(value={"/enque-message","/msg0"})
    @ResponseBody
    public GeneratedMessageV3 enqueMessage(EnqueMessage.EnqueMessageRequest request, EnqueMessage.EnqueMessageResponse.Builder builder){
        String topicName = request.getTopicName();
        long requestId = idGenerator.nextId();
        if(RedisLockUtil.lock(CommonConstant.TOPIC_LOCK_KEY, topicName)) {
            try {
                MessageEntity messageEntity = new MessageEntity();
                messageEntity.setMessageBody(request.getMessage().toByteArray());
                this.upStreamService.enqueMessage(topicName, messageEntity);
                builder.setResult(Result.CommonResult.OK);
            }catch (CommonException ce){
                logger.error("EnqueMessage InnerExecute Error! TopicName:[{}]", topicName, ce);
                builder.setResult(Result.CommonResult.SYSTEM_ERROR);
            }catch (Exception e) {
                logger.error("EnqueMessage Execute Error! TopicName:[{}]", topicName, e);
                builder.setResult(Result.CommonResult.SYSTEM_ERROR);
            }finally {
                RedisLockUtil.unlock(CommonConstant.TOPIC_LOCK_KEY, topicName);
            }
        }else{
            logger.info("EnqueMessage Fetch LockKey Failed! TopicName:[{}]", topicName);
            builder.setResult(Result.CommonResult.CONNECT_TIME_OUT);
        }

        builder.setRequestId(requestId);
        return builder.build();
    }

}
