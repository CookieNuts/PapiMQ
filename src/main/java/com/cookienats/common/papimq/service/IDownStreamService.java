package com.cookienats.common.papimq.service;

import com.cookienats.common.papimq.common.CommonException;
import com.cookienats.common.papimq.common.CommonResult;
import com.cookienats.common.papimq.entity.MessageEntity;

public interface IDownStreamService extends IBaseService {

    public MessageEntity fetchMessage(String topicName, String subscriberName) throws CommonException;


    public void reportConsumeResult(String topicName, String subscriberName, CommonResult commonResult) throws CommonException;

}
