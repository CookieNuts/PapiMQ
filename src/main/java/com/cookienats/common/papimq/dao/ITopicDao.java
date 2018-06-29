package com.cookienats.common.papimq.dao;

import com.cookienats.common.papimq.entity.TopicEntity;

public interface ITopicDao extends IBaseDao {

    public void addTopic(TopicEntity topic);

    public void delTopic(String topicName);

    public Boolean existTopicName(String topicName);

    public void subscribeTopic(String topicName, String subscriberName);

    public void unsubscribeTopic(String topicName, String subscriberName);

    public TopicEntity getTopicInfo(String topicName);

    public void updateTopicInfo(TopicEntity topicEntity);

}
