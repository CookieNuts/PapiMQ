package com.cookienats.common.papimq.service;

import com.cookienats.common.papimq.common.CommonException;
import com.cookienats.common.papimq.entity.SubscriberEntity;
import com.cookienats.common.papimq.entity.TopicEntity;

public interface IMqServerService extends IBaseService {

    public void addTopic(TopicEntity topicEntity) throws CommonException;

    public void delTopic(String topicName) throws CommonException;

    public void addSubscriber(SubscriberEntity subscriber) throws CommonException;

    public void delSubscriber(String subscriberName) throws CommonException;

    public void subscriberTopic(String topicName, String subscriberName);

    public void unsubscriberTopic(String topicName, String subscriberName) throws CommonException;

    //移动消费者消费位置游标，用于回滚消费 跳过消息
    public void moveConsumerOffset(String topicName, String subscriberName, Integer region, Long offset) throws CommonException;

}
