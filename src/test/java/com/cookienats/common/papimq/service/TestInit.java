package com.cookienats.common.papimq.service;

import com.alibaba.fastjson.JSONObject;
import com.cookienats.common.papimq.common.CommonException;
import com.cookienats.common.papimq.entity.SubscriberEntity;
import com.cookienats.common.papimq.entity.TopicEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestInit extends TestBase {
    @Autowired
    private IMqServerService mqServerService;

    @Test
    public void testInit() throws CommonException {
        logger.info("Junit Test Before Start!");
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setTopicName("topic-test01");
        logger.info("add Topic:[{}].", topicEntity.getTopicName());
        mqServerService.addTopic(topicEntity);

        SubscriberEntity subscriberEntity = new SubscriberEntity();
        subscriberEntity.setSubscriberName("subscriber-test01");
        subscriberEntity.setHost("192.168.0.127");
        subscriberEntity.setPort("6666");
        logger.info("add subscriber:[{}].", JSONObject.toJSONString(subscriberEntity));
        mqServerService.addSubscriber(subscriberEntity);

        logger.info("Comsumer:[{}] subscribe Topic:[{}].", subscriberEntity.getSubscriberName(), topicEntity.getTopicName());
        mqServerService.subscriberTopic(topicEntity.getTopicName(), subscriberEntity.getSubscriberName());
        logger.info("Junit Test Before End!");
    }
}
