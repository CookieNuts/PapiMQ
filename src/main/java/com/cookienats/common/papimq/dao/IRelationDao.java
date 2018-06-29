package com.cookienats.common.papimq.dao;

import com.cookienats.common.papimq.entity.RelationEntity;

public interface IRelationDao extends IBaseDao {

    /**
     * 订阅topic后新建relation记录
     * @param topicName
     * @param subscriberName
     * @param relation
     */
    public void addRelation(String topicName, String subscriberName, RelationEntity relation);

    public void delRelation(String topicName, String subscriberName);
    /**
     * 获取读取消息的关系记录
     * @param topicName
     * @param subscriberName
     * @return
     */
    public RelationEntity fetchRelation(String topicName, String subscriberName);

    /**
     * 消费消息后更新对应 region，offset
     * @param topicName
     * @param subscriberName
     * @param region
     * @param offset
     */
    public void updateRelation(String topicName, String subscriberName, RelationEntity relation);
}
