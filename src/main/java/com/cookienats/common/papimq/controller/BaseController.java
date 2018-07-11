package com.cookienats.common.papimq.controller;

import com.cookienats.common.papimq.common.CommonConstant;
import com.cookienats.common.papimq.common.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 当消费者获取Message后返回唯一requestId，并缓存，需要设置超时时间
     * @param requestId
     */
    public void cacheRequestId(String requestId){
        redisUtil.hset(CommonConstant.REDIS_REQUESTID_HASH_MAP, requestId, "0");
        redisUtil.expire(requestId, CommonConstant.REQUESTID_EXPIRE_TIME);
    }

    /**
     * 当消费者返回消费结果时检测requestId是否存在
     * @param requestId
     * @return
     */
    public boolean checkRequestIdExist(String requestId){
        return redisUtil.hexist(CommonConstant.REDIS_REQUESTID_HASH_MAP, requestId);
    }

    /**
     * 当消费者返回消费结果时检测requestId是否存在，完成消费后再删除requestId
     * @param requestId
     * @return
     */
    public void delRequestId(String requestId){
        redisUtil.hdel(CommonConstant.REDIS_REQUESTID_HASH_MAP, requestId);
    }

}
