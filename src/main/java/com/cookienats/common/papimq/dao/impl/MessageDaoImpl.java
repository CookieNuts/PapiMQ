package com.cookienats.common.papimq.dao.impl;

import com.cookienats.common.papimq.common.utils.KryoUtil;
import com.cookienats.common.papimq.dao.IMessageDao;
import com.cookienats.common.papimq.entity.MessageEntity;
import org.springframework.stereotype.Component;

@Component
public class MessageDaoImpl extends BaseDaoImpl implements IMessageDao {

    @Override
    public Long getMessageQueueLength(String topicName, Integer region) {
        String messageHashKey = this.generateMessageHashKey(topicName, region);
        return this.redisUtil.llen(messageHashKey);
    }

    @Override
    public void enqueMessage(String topicName, Integer region, MessageEntity message) {
        String messageHashKey = this.generateMessageHashKey(topicName, region);
        this.redisUtil.EnQueue(messageHashKey, message);
    }

    @Override
    public MessageEntity indexGetMessage(String topicName, String subscriberName, Integer region, Long offset) {
        MessageEntity result = null;
        String messageHashKey = this.generateMessageHashKey(topicName, region);
        byte[] arr = this.redisUtil.lGet(messageHashKey, offset);
        if(arr != null && arr.length > 0){
            result = KryoUtil.readObjectFromByteArray(arr, MessageEntity.class);
        }
        return  result;
    }
}
