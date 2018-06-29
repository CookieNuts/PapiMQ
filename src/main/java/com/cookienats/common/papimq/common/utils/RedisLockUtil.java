package com.cookienats.common.papimq.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.Time;

@Component
public class RedisLockUtil {

    private static final int RETRY_TIMES = 10;
    private static final int TIME_OUT = 30 * 1000;

    protected static RedisUtil staticRedisUtil;

    @Autowired
    protected RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        staticRedisUtil = redisUtil;
    }


    public static boolean lock(String key, String requestId){
        try {
            int i = 0;
            while ( i < RETRY_TIMES){
                if(staticRedisUtil.tryGetDistributedLock(key, requestId, TIME_OUT)){
                    return true;
                }
                Thread.sleep(100);
                i++;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static boolean unlock(String key, String requestId){
        return staticRedisUtil.releaseDistributedLock(key, requestId);
    }

}
