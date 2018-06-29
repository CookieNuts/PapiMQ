package com.cookienats.common.papimq.dao;

import com.cookienats.common.papimq.entity.MessageEntity;

import java.util.Set;

/**
 *  每个topic对应一个message队列， message队列根据条数拆分region
 */
public interface IMessageDao extends IBaseDao {

    public Long getMessageQueueLength(String topicName, Integer region);

    /**
     * 消息入队
     * @param topicName 消息根据topic分hash存储，切按消息数量切分多个region
     * @param region
     */
    public void enqueMessage(String topicName, Integer region, MessageEntity message);

    /**
     * 获取index消息
     * @param topicName
     * @param subscriberName
     * @param region
     * @param offset
     * @return
     */
    public MessageEntity indexGetMessage(String topicName, String subscriberName, Integer region, Long offset);

}
