package com.cookienats.common.papimq.dao;

import com.cookienats.common.papimq.entity.SubscriberEntity;

public interface ISubscriberDao extends IBaseDao {
    public void addSubscriber(SubscriberEntity subscriber);

    public void delSubscriber(String subscirberName);

    public Boolean existSubscriber(String subscirberName);

    public SubscriberEntity getSubscriberInfo(String subscirberName);

    public void updateSubscriber(SubscriberEntity subscriber);
}
