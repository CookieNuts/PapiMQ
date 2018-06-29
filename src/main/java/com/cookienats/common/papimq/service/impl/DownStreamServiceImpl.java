package com.cookienats.common.papimq.service.impl;

import com.cookienats.common.papimq.common.CommonConstant;
import com.cookienats.common.papimq.common.CommonErrorCode;
import com.cookienats.common.papimq.common.CommonException;
import com.cookienats.common.papimq.common.CommonResult;
import com.cookienats.common.papimq.dao.IMessageDao;
import com.cookienats.common.papimq.dao.IRelationDao;
import com.cookienats.common.papimq.dao.ITopicDao;
import com.cookienats.common.papimq.entity.MessageEntity;
import com.cookienats.common.papimq.entity.RelationEntity;
import com.cookienats.common.papimq.entity.TopicEntity;
import com.cookienats.common.papimq.service.IDownStreamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownStreamServiceImpl extends BaseServiceImpl implements IDownStreamService {

    @Autowired
    private IRelationDao relationDao;
    @Autowired
    private IMessageDao messageDao;
    @Autowired
    private ITopicDao topicDao;

    @Override
    public MessageEntity fetchMessage(String topicName, String subscriberName)  throws CommonException{
        MessageEntity messageEntity = null;
        RelationEntity relationEntity = relationDao.fetchRelation(topicName, subscriberName);
        TopicEntity topicEntity = topicDao.getTopicInfo(topicName);
        if(topicEntity == null){
            throw new CommonException(CommonErrorCode.DEQUE_MESSAGE_ERROR, " 获取Topic不存在！");
        }else if(relationEntity == null){
            throw new CommonException(CommonErrorCode.DEQUE_MESSAGE_ERROR, " 获取Relation不存在！");
        }else{
            Integer region = relationEntity.getRegion();
            Long offset = relationEntity.getOffset();
            logger.info("[Fetch] Message Before. topicName:[{}] subscriberName:[{}] region:[{}] offset:[{}] ", topicName, subscriberName, region, offset);
            messageEntity = messageDao.indexGetMessage(topicName, subscriberName, region, offset);
            logger.info("[Fetch] Message After. topicName:[{}] subscriberName:[{}] Result: {} ", topicName, subscriberName, messageEntity);
        }
        return messageEntity;
    }

    @Override
    public void reportConsumeResult(String topicName, String subscriberName, CommonResult commonResult) throws CommonException {
        RelationEntity relationEntity = relationDao.fetchRelation(topicName, subscriberName);
        TopicEntity topicEntity = topicDao.getTopicInfo(topicName);
        if(topicEntity == null){
            throw new CommonException(CommonErrorCode.DSTREAM_REPORT_FAIL_ERROR, " 获取Topic不存在！");
        }else if(relationEntity == null){
            throw new CommonException(CommonErrorCode.DSTREAM_REPORT_FAIL_ERROR, " 获取Relation不存在！");
        }else{
            Integer expectRegion = relationEntity.getRegion();
            Long expectOffset = relationEntity.getOffset();
            logger.info("[Report] Consume Message Result:[{}] Before. topicName:[{}] subscriberName:[{}] region:[{}] offset:[{}] "
                    , topicName, subscriberName, commonResult, expectRegion, expectOffset);

            //查询当前topic当前region
            Integer currentRegion = topicEntity.getCurrentRegion();
            //查询当前topic消息最后一条记录的region的id
            Long maxId = messageDao.getMessageQueueLength(topicName,currentRegion) - 1;
            if(currentRegion == expectRegion && expectOffset > maxId){
                //如果offset > 最后一条消息id，则不允许report操作
                throw new CommonException(CommonErrorCode.DSTREAM_REPORT_FAIL_ERROR," 消息记录不存在！");
            }

            switch (commonResult){
                case CONSUME_SUCCESS:           //消费成功offset指向下一条消息
                    //读取到Message的region最后一条记录，需要从下一个region开始读取
                    if(expectRegion < currentRegion && expectOffset + 1 >= CommonConstant.REGION_MAX_MESSAGE_COUNTS){
                        expectRegion += 1;
                        expectOffset = 0L;
                    }else{
                        //如果读取到最后一个region的最后一条消息，offset也会+1，且下次获取时读空消息
                        expectOffset += 1;
                    }
                    relationEntity.setRegion(expectRegion);
                    relationEntity.setOffset(expectOffset);
                    relationEntity.setFailNum(0);
                    break;

                case CONSUME_FAIL:              //消费失败卡在当前offset消息，并累计失败次数
                    relationEntity.setFailNum(relationEntity.getFailNum() + 1);
                    break;

                default:
                    throw new CommonException(CommonErrorCode.DSTREAM_REPORT_FAIL_ERROR, " Message消费结果有误！");
            }
            logger.info("[Report] Consume Message Result:[{}] After. topicName:[{}] subscriberName:[{}] region:[{}] offset:[{}] failCount:[{}]"
                    , topicName, subscriberName, commonResult, relationEntity.getRegion(), relationEntity.getOffset(), relationEntity.getFailNum());

            relationDao.updateRelation(topicName, subscriberName,  relationEntity);
        }
    }
}
