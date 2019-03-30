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
    public void testAddTopic() throws CommonException {
        logger.info("Junit Test Before Start!");
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setTopicName("topic-test1");
        logger.info("add Topic:[{}].", topicEntity.getTopicName());
        mqServerService.addTopic(topicEntity);
    }

    public void testAddSubscriber(Integer subscriberIndex) throws CommonException {
        SubscriberEntity subscriberEntity = new SubscriberEntity();
        subscriberEntity.setSubscriberName("subscriber-test" + subscriberIndex);
        subscriberEntity.setHost("192.168.0.127");
        subscriberEntity.setPort("6666");
        logger.info("add subscriber:[{}].", JSONObject.toJSONString(subscriberEntity));
        mqServerService.addSubscriber(subscriberEntity);

        logger.info("Comsumer:[{}] subscribe Topic:[{}].", subscriberEntity.getSubscriberName(), "topic-test1");
        mqServerService.subscriberTopic("topic-test1", subscriberEntity.getSubscriberName());
        logger.info("Junit Test Before End!");
    }

    @Test
    public void testBatchAddSubscriber() throws CommonException {
        for (int i = 0; i < 50; i++){
            testAddSubscriber(i+1);
        }
    }

    @Test
    public void testInit() throws CommonException {
        logger.info("Junit Test Before Start!");
        TopicEntity topicEntity = new TopicEntity();
        topicEntity.setTopicName("topic-test1");
        logger.info("add Topic:[{}].", topicEntity.getTopicName());
        mqServerService.addTopic(topicEntity);

        SubscriberEntity subscriberEntity = new SubscriberEntity();
        subscriberEntity.setSubscriberName("subscriber-test1");
        subscriberEntity.setHost("192.168.0.127");
        subscriberEntity.setPort("6666");
        logger.info("add subscriber:[{}].", JSONObject.toJSONString(subscriberEntity));
        mqServerService.addSubscriber(subscriberEntity);

        logger.info("Comsumer:[{}] subscribe Topic:[{}].", subscriberEntity.getSubscriberName(), topicEntity.getTopicName());
        mqServerService.subscriberTopic(topicEntity.getTopicName(), subscriberEntity.getSubscriberName());
        logger.info("Junit Test Before End!");
    }
}
