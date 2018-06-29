package com.cookienats.common.papimq.service;

import com.alibaba.fastjson.JSONObject;
import com.cookienats.common.papimq.common.CommonException;
import com.cookienats.common.papimq.common.utils.KryoUtil;
import com.cookienats.common.papimq.entity.MessageEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUpStreamService extends TestBase {
    @Autowired
    private IUpStreamService upStreamService;

    @Test
    public void testEnqueMessage() throws CommonException {
        MessageEntity messageEntity = new MessageEntity();
        List<String> list = new ArrayList<>();
        list.add("tete");
        list.add("1212");
        messageEntity.setMessageBody(KryoUtil.writeObjectToByteArray(list));
        upStreamService.enqueMessage("topic-test01", messageEntity);
    }

    @Test
    public void testBatchEnqueMessage() throws CommonException {
        MessageEntity temp = null;
        Map<String, Object> map = null;
        for (int i = 0; i < 16; i++){
            temp = new MessageEntity();
            map = new HashMap<>();
            map.put("title","title"+i);
            map.put("value", "value"+i);
            temp.setMessageBody(KryoUtil.writeObjectToByteArray(map));
            upStreamService.enqueMessage("topic-test01", temp);
            logger.info("[Enque] Message: {}", JSONObject.toJSONString(temp));
        }
    }
}
