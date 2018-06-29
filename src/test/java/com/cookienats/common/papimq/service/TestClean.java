package com.cookienats.common.papimq.service;

import com.cookienats.common.papimq.common.CommonException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TestClean extends TestBase{
    @Autowired
    private IMqServerService mqServerService;

    @Test
    public void testClean() throws CommonException {
        logger.info("Junit Test After Start!");
        logger.info("Clean relation!");
        mqServerService.unsubscriberTopic("topic-test01", "subscriber-test01");
        logger.info("Clean subscriber!");
        mqServerService.delSubscriber("subscriber-test01");
        logger.info("Clean topic!");
        mqServerService.delTopic("topic-test01");
        logger.info("Junit Test After End!");
    }
}
