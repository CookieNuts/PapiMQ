package com.cookienats.common.papimq.entity;

import java.io.Serializable;

public class SubscriberEntity implements Serializable {
    private String subscriberName;
    private String host;
    private String port;

    public String getSubscriberName() {
        return subscriberName;
    }

    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
