package com.cookienats.common.papimq.dao;

public interface IBaseDao {
    public String generateMessageHashKey(String topicName, Integer region);

    public String generateRelation(String topicName, String subscriberName);
}
