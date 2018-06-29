package com.cookienats.common.papimq.dao.impl;

import com.cookienats.common.papimq.common.CommonConstant;
import com.cookienats.common.papimq.common.utils.KryoUtil;
import com.cookienats.common.papimq.dao.IRelationDao;
import com.cookienats.common.papimq.entity.RelationEntity;
import org.springframework.stereotype.Component;

@Component
public class RelationDaoImpl extends BaseDaoImpl implements IRelationDao{

    @Override
    public void addRelation(String topicName, String subscriberName, RelationEntity relation){
        String relationHashKey = this.generateRelation(topicName, subscriberName);
        this.redisUtil.hsetnx(CommonConstant.REDIS_RELATION_HASH_MAP, relationHashKey.getBytes(), KryoUtil.writeObjectToByteArray(relation));
    }

    @Override
    public void delRelation(String topicName, String subscriberName){
        String relationHashKey = this.generateRelation(topicName, subscriberName);
        this.redisUtil.hdel(CommonConstant.REDIS_RELATION_HASH_MAP, relationHashKey.getBytes());
    }

    @Override
    public RelationEntity fetchRelation(String topicName, String subscriberName) {
        RelationEntity relationEntity = null;
        String relationHashKey = this.generateRelation(topicName, subscriberName);
        byte[] arr =  this.redisUtil.hget(CommonConstant.REDIS_RELATION_HASH_MAP, relationHashKey.getBytes());
        if(arr != null || arr.length > 0){
            relationEntity = KryoUtil.readObjectFromByteArray(arr, RelationEntity.class);
        }
        return relationEntity;
    }

    @Override
    public void updateRelation(String topicName, String subscriberName, RelationEntity relation) {
        String relationHashKey = this.generateRelation(topicName, subscriberName);
        this.redisUtil.hset(CommonConstant.REDIS_RELATION_HASH_MAP, relationHashKey.getBytes(), KryoUtil.writeObjectToByteArray(relation));
    }
}
