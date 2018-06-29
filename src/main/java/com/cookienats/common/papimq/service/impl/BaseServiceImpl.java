package com.cookienats.common.papimq.service.impl;

import com.cookienats.common.papimq.common.utils.KryoUtil;
import com.cookienats.common.papimq.common.utils.RedisUtil;
import com.cookienats.common.papimq.service.IBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("baseService")
public class BaseServiceImpl implements IBaseService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public RedisUtil redisUtil;
    @Autowired
    public KryoUtil kryoUtil;
}
