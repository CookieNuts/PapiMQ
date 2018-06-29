package com.cookienats.common.papimq.dao.impl;

import com.cookienats.common.papimq.common.CommonConstant;
import com.cookienats.common.papimq.common.utils.KryoUtil;
import com.cookienats.common.papimq.dao.ISubscriberDao;
import com.cookienats.common.papimq.entity.SubscriberEntity;
import org.springframework.stereotype.Component;

@Component
public class SubscriberDaoImpl extends BaseDaoImpl implements ISubscriberDao {


    @Override
    public void addSubscriber(SubscriberEntity subscriber) {
        this.redisUtil.hsetnx(CommonConstant.REDIS_SUBSCRIBER_HASH_MAP, subscriber.getSubscriberName().getBytes(), KryoUtil.writeObjectToByteArray(subscriber));
    }

    @Override
    public void delSubscriber(String subscirberName) {
        this.redisUtil.hdel(CommonConstant.REDIS_SUBSCRIBER_HASH_MAP, subscirberName.getBytes());
    }

    @Override
    public Boolean existSubscriber(String subscirberName) {
       return this.redisUtil.hexist(CommonConstant.REDIS_SUBSCRIBER_HASH_MAP, subscirberName.getBytes());
    }

    @Override
    public SubscriberEntity getSubscriberInfo(String subscirberName) {
        SubscriberEntity subscriberEntity = null;
        byte[] arr = this.redisUtil.hget(CommonConstant.REDIS_SUBSCRIBER_HASH_MAP, subscirberName.getBytes());
        if(arr != null || arr.length > 0){
            subscriberEntity = KryoUtil.readObjectFromByteArray(arr, SubscriberEntity.class);
        }
        return subscriberEntity;
    }

    @Override
    public void updateSubscriber(SubscriberEntity subscriber) {
        this.redisUtil.hset(CommonConstant.REDIS_SUBSCRIBER_HASH_MAP, subscriber.getSubscriberName().getBytes(), KryoUtil.writeObjectToByteArray(subscriber));
    }
}
