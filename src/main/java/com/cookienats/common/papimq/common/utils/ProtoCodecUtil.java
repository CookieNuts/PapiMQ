package com.cookienats.common.papimq.common.utils;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Parser;

public class ProtoCodecUtil {
    public static GeneratedMessageV3 decodeOnlyObject(byte[] data, Parser<? extends GeneratedMessageV3> parser) throws InvalidProtocolBufferException {
        return parser.parseFrom(data);
    }
}
