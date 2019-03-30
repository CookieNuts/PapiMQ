package com.cookienats.common.papimq.socket;

import com.cookienats.common.papimq.protos.common.Enums;
import com.cookienats.common.papimq.protos.msg.consumer.FetchMessage;
import com.cookienats.common.papimq.protos.msg.consumer.ReportConsumeResult;
import com.cookienats.common.papimq.protos.msg.producer.EnqueMessage;
import com.google.protobuf.Parser;

import java.util.HashMap;
import java.util.Map;

public class MsgIdProtoParser {

    private static Map<Integer, MsgProtoWapper> msgHashMap = new HashMap<Integer, MsgProtoWapper>();

    private static MsgIdProtoParser instance;

    private MsgIdProtoParser(){
        msgHashMap.put(Enums.MsgId.ENQUE_MESSAGE_VALUE, new MsgProtoWapper(Enums.MsgId.ENQUE_MESSAGE, "ProducerController$$enqueMessage", EnqueMessage.EnqueMessageRequest.class
                , EnqueMessage.EnqueMessageResponse.class, EnqueMessage.EnqueMessageRequest.parser(), EnqueMessage.EnqueMessageResponse.parser()));
        msgHashMap.put(Enums.MsgId.FETCH_MESSAGE_VALUE, new MsgProtoWapper(Enums.MsgId.FETCH_MESSAGE, "ComsumerController$$fetchMessage", FetchMessage.FetchMessageRequest.class, FetchMessage.FetchMessageResponse.class
                , FetchMessage.FetchMessageRequest.parser(), FetchMessage.FetchMessageResponse.parser()));
        msgHashMap.put(Enums.MsgId.REPORT_COMSUME_VALUE, new MsgProtoWapper(Enums.MsgId.REPORT_COMSUME, "ComsumerController$$reportConsume", ReportConsumeResult.ReportConsumeResultRequest.class, ReportConsumeResult.ReportConsumeResultResponse.class
                , ReportConsumeResult.ReportConsumeResultRequest.parser(), ReportConsumeResult.ReportConsumeResultResponse.parser()));
    }

    public static MsgIdProtoParser getInstance(){
        if(instance == null){
            instance = new MsgIdProtoParser();
        }
        return instance;
    }

    public MsgProtoWapper getMsgProtoWapperByMsgId(Integer msgId){
        return msgHashMap.get(msgId);
    }

    public Boolean containsMsgId(Integer msgId){
        return msgHashMap.containsKey(msgId);
    }

    class MsgProtoWapper{
        private Enums.MsgId msgId;
        private String actionName;
        private Class requestClass;
        private Class responseClass;
        private Parser requestParser;
        private Parser rewponseParser;

        public Enums.MsgId getMsgId() {
            return msgId;
        }

        public void setMsgId(Enums.MsgId msgId) {
            this.msgId = msgId;
        }

        public String getActionName() {
            return actionName;
        }

        public void setActionName(String actionName) {
            this.actionName = actionName;
        }

        public Class getRequestClass() {
            return requestClass;
        }

        public void setRequestClass(Class requestClass) {
            this.requestClass = requestClass;
        }

        public Class getResponseClass() {
            return responseClass;
        }

        public void setResponseClass(Class responseClass) {
            this.responseClass = responseClass;
        }

        public Parser getRequestParser() {
            return requestParser;
        }

        public void setRequestParser(Parser requestParser) {
            this.requestParser = requestParser;
        }

        public Parser getRewponseParser() {
            return rewponseParser;
        }

        public void setRewponseParser(Parser rewponseParser) {
            this.rewponseParser = rewponseParser;
        }

        public MsgProtoWapper(Enums.MsgId msgId, String actionName, Class requestClass, Class responseClass, Parser requestParser, Parser rewponseParser) {
            this.msgId = msgId;
            this.actionName = actionName;
            this.requestClass = requestClass;
            this.responseClass = responseClass;
            this.requestParser = requestParser;
            this.rewponseParser = rewponseParser;
        }
    }



}
