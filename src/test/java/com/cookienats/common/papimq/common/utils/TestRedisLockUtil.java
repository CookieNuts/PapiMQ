package com.cookienats.common.papimq.common.utils;

import com.cookienats.common.papimq.service.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

public class TestRedisLockUtil extends TestBase {

    private final String lockKey = "TestLockKey";

    @Test
    public void testRedisLock() throws InterruptedException {
       long startTime = System.currentTimeMillis();

       Thread thread = null;
       for (int i = 0; i < 20; i++){
            logger.info("-----------i--------"+i);
            thread = new Thread(new Runnable() {
               @Override
               public void run() {
                   String requestId = String.valueOf(idGenerator.nextId());
                   while (true){
                       if(RedisLockUtil.lock(lockKey, requestId)) {
                           logger.info("Thread: [{}] fetch LockKey: [{}] requestId: [{}] Success!"
                                   , Thread.currentThread().getName(), lockKey, requestId);
                           try {
                               logger.info("Logic Code Running!");
                               Thread.sleep(1000);
                               logger.info("Logic Code End!");
                           }catch (Exception e){
                               logger.error("Logic Code Exception!", e);
                           }finally {
                               RedisLockUtil.unlock(lockKey, requestId);
                               try {
                                   Thread.sleep(200);
                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               }
                           }
                       }else {
                           logger.info("Thread: [{}] fetch LockKey: [{}] requestId: [{}] Fail!"
                                   , Thread.currentThread().getName(), lockKey, requestId);
                       }
                   }
               }
           },"thread-"+i);
           thread.start();
       }

       Thread.sleep(30*1000);
    }

}
