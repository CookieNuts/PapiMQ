package com.cookienats.common.papimq.dao.impl;

import com.cookienats.common.papimq.common.CommonConstant;
import com.cookienats.common.papimq.common.utils.KryoUtil;
import com.cookienats.common.papimq.common.utils.RedisUtil;
import com.cookienats.common.papimq.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseDaoImpl implements IBaseDao{
    @Autowired
    public RedisUtil redisUtil;
    @Autowired
    public KryoUtil kryoUtil;
    @Autowired
    public IMessageDao messageDao;
    @Autowired
    public IRelationDao relationDao;
    @Autowired
    public ISubscriberDao subscriberDao;
    @Autowired
    public ITopicDao topicDao;

    @Override
    public String generateMessageHashKey(String topicName, Integer region) {
        return CommonConstant.REDIS_MESSAGE_LIST_PREFIX + topicName +"_"+ region;
    }

    @Override
    public String generateRelation(String topicName, String subscriberName) {
        return CommonConstant.REDIS_RELATION_HASH_KEY_PREFIX + topicName +"_"+ subscriberName;
    }
}
