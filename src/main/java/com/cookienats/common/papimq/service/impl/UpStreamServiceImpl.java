package com.cookienats.common.papimq.service.impl;

import com.cookienats.common.papimq.common.CommonConstant;
import com.cookienats.common.papimq.common.CommonErrorCode;
import com.cookienats.common.papimq.common.CommonException;
import com.cookienats.common.papimq.dao.IMessageDao;
import com.cookienats.common.papimq.dao.IRelationDao;
import com.cookienats.common.papimq.dao.ITopicDao;
import com.cookienats.common.papimq.entity.MessageEntity;
import com.cookienats.common.papimq.entity.TopicEntity;
import com.cookienats.common.papimq.service.IUpStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpStreamServiceImpl extends BaseServiceImpl implements IUpStreamService {

    @Autowired
    private IMessageDao messageDao;
    @Autowired
    private ITopicDao topicDao;
    @Autowired
    private IRelationDao relationDao;

    @Override
    public void enqueMessage(String topicName, MessageEntity message) throws CommonException {
        /**
         * 1.查询topic的regionId，获得最大分区id
         * 2.判断最大分区中消息数是否超过限制，是：创建新的分区更新regionId字段，入队消息 否：入队消息
         */
        TopicEntity topicEntity = topicDao.getTopicInfo(topicName);
        if(topicEntity == null){
            throw new CommonException(CommonErrorCode.ENQUE_MESSAGE_ERROR, "获取Topic信息为空！");
        }else{
            Integer region = topicEntity.getCurrentRegion();
            Long messageCount = messageDao.getMessageQueueLength(topicName, region);
            if(messageCount == null){
                throw new CommonException(CommonErrorCode.ENQUE_MESSAGE_ERROR, "获取消息队列长度为空！");
            }
            if(messageCount >= CommonConstant.REGION_MAX_MESSAGE_COUNTS){    //消息数大于等于region最大消息数则新建分区
                region += 1;
                messageCount = 0L;
                topicEntity.setCurrentRegion(region);
                topicDao.updateTopicInfo(topicEntity);
            }
            logger.info("[Enque] Message. topicName:[{}] currentRegion:[{}] currentIndex:[{}]", topicName, region,messageCount);
            message.setId(messageCount);    //id从0开始，当前消息长度即为下一条消息id
            messageDao.enqueMessage(topicName, region, message);
        }
    }
}
