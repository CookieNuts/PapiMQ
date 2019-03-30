package com.cookienats.common.papimq.service;

import com.cookienats.common.papimq.common.CommonException;
import com.cookienats.common.papimq.common.utils.KryoUtil;
import com.cookienats.common.papimq.entity.MessageEntity;
import com.cookienats.common.papimq.protos.common.Enums;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class TestDownStreamService extends TestBase {
    @Autowired
    private IDownStreamService downStreamService;

    @Test
    public void testFetchMessage() throws CommonException {
        MessageEntity messageEntity = downStreamService.fetchMessage("topic-test1", "subscriber-test1");
        logger.info("[Fetch] Message: {}", KryoUtil.readObjectFromByteArray(messageEntity.getMessageBody(), HashMap.class));
    }

    @Test
    public void testReportSuccessConsumeResult() throws CommonException  {
        downStreamService.reportConsumeResult("topic-test1", "subscriber-test1", Enums.ConsumeResult.CONSUME_SUCCESS);
    }

    @Test
    public void testReportFailConsumeResult() throws CommonException  {
        downStreamService.reportConsumeResult("topic-test1", "subscriber-test1", Enums.ConsumeResult.CONSUME_FAIL);
    }

}
