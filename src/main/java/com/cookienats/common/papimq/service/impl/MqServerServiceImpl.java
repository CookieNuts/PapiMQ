package com.cookienats.common.papimq.service.impl;

import com.cookienats.common.papimq.common.CommonErrorCode;
import com.cookienats.common.papimq.common.CommonException;
import com.cookienats.common.papimq.dao.IMessageDao;
import com.cookienats.common.papimq.dao.IRelationDao;
import com.cookienats.common.papimq.dao.ISubscriberDao;
import com.cookienats.common.papimq.dao.ITopicDao;
import com.cookienats.common.papimq.entity.RelationEntity;
import com.cookienats.common.papimq.entity.SubscriberEntity;
import com.cookienats.common.papimq.entity.TopicEntity;
import com.cookienats.common.papimq.service.IMqServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MqServerServiceImpl extends BaseServiceImpl implements IMqServerService {
    @Autowired
    private ITopicDao topicDao;
    @Autowired
    private ISubscriberDao subscriberDao;
    @Autowired
    private IRelationDao relationDao;
    @Autowired
    private IMessageDao messageDao;

    @Override
    public void addTopic(TopicEntity topicEntity) throws CommonException {
        if(topicDao.existTopicName(topicEntity.getTopicName())){
            throw new CommonException(CommonErrorCode.SERVICE_ADD_TOPIC_ERROR, " Topic信息已存在! ");
        }
        topicEntity.setCreateTime(new Date().getTime());
        topicEntity.setUpdateTime(new Date().getTime());
        topicEntity.setCurrentRegion(0);
        topicDao.addTopic(topicEntity);
    }

    @Override
    public void delTopic(String topicName) throws CommonException{
        if(topicDao.existTopicName(topicName)){
            topicDao.delTopic(topicName);
        }else{
            throw new CommonException(CommonErrorCode.SERVICE_DEL_TOPIC_ERROR, " Topic信息不存在！");
        }
    }

    @Override
    public void addSubscriber(SubscriberEntity subscriber) throws CommonException {
        if(subscriberDao.existSubscriber(subscriber.getSubscriberName())){
            throw new CommonException(CommonErrorCode.SERVICE_ADD_SUBSCRIBER_ERROR, " Subscriber信息已存在! ");
        }
        subscriberDao.addSubscriber(subscriber);
    }

    @Override
    public void delSubscriber(String subscriberName) throws CommonException{
        if(subscriberDao.existSubscriber(subscriberName)){
            subscriberDao.delSubscriber(subscriberName);
        }else{
            throw new CommonException(CommonErrorCode.SERVICE_DEL_SUBSCRIBER_ERROR, " Subscriber信息不存在！");
        }
    }

    @Override
    public void subscriberTopic(String topicName, String subscriberName) {
        Long currentTime = new Date().getTime();
        RelationEntity relationEntity = new RelationEntity();
        relationEntity.setRegion(0);
        relationEntity.setOffset(0L);
        relationEntity.setFailNum(0);
        relationEntity.setCreateTime(currentTime);
        relationEntity.setSubscriberName(subscriberName);
        relationEntity.setTopicName(topicName);
        relationEntity.setUpdateTime(currentTime);
        relationDao.addRelation(topicName, subscriberName, relationEntity);
    }

    @Override
    public void unsubscriberTopic(String topicName, String subscriberName) throws CommonException {
        RelationEntity relationEntity = relationDao.fetchRelation(topicName, subscriberName);
        if(relationEntity != null){
            relationDao.delRelation(topicName, subscriberName);
        }else{
            throw new CommonException(CommonErrorCode.SERVICE_UNSUBSCRIBER_ERROR, " Topic与Subscriber关联关系不存在！");
        }
    }

    @Override
    public void moveConsumerOffset(String topicName, String subscriberName, Integer region, Long offset) throws CommonException {
        RelationEntity relationEntity = relationDao.fetchRelation(topicName, subscriberName);
        if(relationEntity != null){
            TopicEntity topic = topicDao.getTopicInfo(topicName);
            if(topic != null){
                int maxRegion = topic.getCurrentRegion();
                if(region > maxRegion){
                    throw new CommonException(CommonErrorCode.SERVICE_MOVE_OFFSET_ERROR, " 修改Region超过当前最大值! ");
                }else{
                    relationEntity.setRegion(region);
                    relationEntity.setOffset(offset);
                    relationDao.updateRelation(topicName, subscriberName, relationEntity);
                }
            }else{
                throw new CommonException(CommonErrorCode.SERVICE_MOVE_OFFSET_ERROR, " Topic不存在! ");
            }
        }else{
            throw new CommonException(CommonErrorCode.SERVICE_UNSUBSCRIBER_ERROR, " Topic与Subscriber关联关系不存在！");
        }
    }
}
