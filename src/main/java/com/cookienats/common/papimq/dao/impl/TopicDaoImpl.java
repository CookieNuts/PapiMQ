package com.cookienats.common.papimq.dao.impl;

import com.cookienats.common.papimq.common.CommonConstant;
import com.cookienats.common.papimq.common.utils.KryoUtil;
import com.cookienats.common.papimq.dao.ITopicDao;
import com.cookienats.common.papimq.entity.TopicEntity;
import org.springframework.stereotype.Component;

@Component
public class TopicDaoImpl extends BaseDaoImpl implements ITopicDao {

    @Override
    public void addTopic(TopicEntity topic) {
        this.redisUtil.hsetnx(CommonConstant.REDIS_TOPIC_HASH_MAP, topic.getTopicName().getBytes(), KryoUtil.writeObjectToByteArray(topic));
    }

    @Override
    public void delTopic(String topicName) {
        this.redisUtil.hdel(CommonConstant.REDIS_TOPIC_HASH_MAP, topicName.getBytes());
    }

    @Override
    public Boolean existTopicName(String topicName) {
        return this.redisUtil.hexist(CommonConstant.REDIS_TOPIC_HASH_MAP, topicName.getBytes());
    }

    @Override
    public void subscribeTopic(String topicName, String subscriberName) {

    }

    @Override
    public void unsubscribeTopic(String topicName, String subscriberName) {

    }

    @Override
    public TopicEntity getTopicInfo(String topicName){
        TopicEntity topicEntity = null;
        byte[] topicByte = this.redisUtil.hget(CommonConstant.REDIS_TOPIC_HASH_MAP, topicName.getBytes());
        if(topicByte != null && topicByte.length > 0) {
            topicEntity = KryoUtil.readObjectFromByteArray(topicByte, TopicEntity.class);
        }
        return topicEntity;
    }

    @Override
    public void updateTopicInfo(TopicEntity topic) {
        this.redisUtil.hset(CommonConstant.REDIS_TOPIC_HASH_MAP, topic.getTopicName().getBytes(), KryoUtil.writeObjectToByteArray(topic));
    }
}
