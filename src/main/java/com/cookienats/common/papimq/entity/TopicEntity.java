package com.cookienats.common.papimq.entity;

import java.io.Serializable;

public class TopicEntity implements Serializable {
    private String topicName;
    //topic对应消息队列当前regionId(从0开始)，预设每100万拆分region，并更新此字段
    private Integer currentRegion;
    private Long createTime;
    private Long updateTime;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCurrentRegion() {
        return currentRegion;
    }

    public void setCurrentRegion(Integer currentRegion) {
        this.currentRegion = currentRegion;
    }
}
