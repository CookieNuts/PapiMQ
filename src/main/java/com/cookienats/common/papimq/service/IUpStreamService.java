package com.cookienats.common.papimq.service;

import com.cookienats.common.papimq.common.CommonException;
import com.cookienats.common.papimq.entity.MessageEntity;

public interface IUpStreamService extends IBaseService {

    public void enqueMessage(String topicName, MessageEntity message) throws CommonException;
}
