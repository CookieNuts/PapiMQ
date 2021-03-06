// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: common/Enums.proto

package com.cookienats.common.papimq.protos.common;

public final class Enums {
  private Enums() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  /**
   * Protobuf enum {@code ConsumeResult}
   */
  public enum ConsumeResult
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <pre>
     *消费失败
     * </pre>
     *
     * <code>CONSUME_FAIL = 0;</code>
     */
    CONSUME_FAIL(0),
    /**
     * <pre>
     *消费成功
     * </pre>
     *
     * <code>CONSUME_SUCCESS = 1;</code>
     */
    CONSUME_SUCCESS(1),
    UNRECOGNIZED(-1),
    ;

    /**
     * <pre>
     *消费失败
     * </pre>
     *
     * <code>CONSUME_FAIL = 0;</code>
     */
    public static final int CONSUME_FAIL_VALUE = 0;
    /**
     * <pre>
     *消费成功
     * </pre>
     *
     * <code>CONSUME_SUCCESS = 1;</code>
     */
    public static final int CONSUME_SUCCESS_VALUE = 1;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @Deprecated
    public static ConsumeResult valueOf(int value) {
      return forNumber(value);
    }

    public static ConsumeResult forNumber(int value) {
      switch (value) {
        case 0: return CONSUME_FAIL;
        case 1: return CONSUME_SUCCESS;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<ConsumeResult>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        ConsumeResult> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<ConsumeResult>() {
            public ConsumeResult findValueByNumber(int number) {
              return ConsumeResult.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return Enums.getDescriptor().getEnumTypes().get(0);
    }

    private static final ConsumeResult[] VALUES = values();

    public static ConsumeResult valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private ConsumeResult(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:ConsumeResult)
  }

  /**
   * Protobuf enum {@code MsgId}
   */
  public enum MsgId
      implements com.google.protobuf.ProtocolMessageEnum {
    /**
     * <pre>
     *上游入队消息
     * </pre>
     *
     * <code>ENQUE_MESSAGE = 0;</code>
     */
    ENQUE_MESSAGE(0),
    /**
     * <pre>
     *下游获取消息
     * </pre>
     *
     * <code>FETCH_MESSAGE = 1;</code>
     */
    FETCH_MESSAGE(1),
    /**
     * <pre>
     *下游汇报消费结果
     * </pre>
     *
     * <code>REPORT_COMSUME = 2;</code>
     */
    REPORT_COMSUME(2),
    UNRECOGNIZED(-1),
    ;

    /**
     * <pre>
     *上游入队消息
     * </pre>
     *
     * <code>ENQUE_MESSAGE = 0;</code>
     */
    public static final int ENQUE_MESSAGE_VALUE = 0;
    /**
     * <pre>
     *下游获取消息
     * </pre>
     *
     * <code>FETCH_MESSAGE = 1;</code>
     */
    public static final int FETCH_MESSAGE_VALUE = 1;
    /**
     * <pre>
     *下游汇报消费结果
     * </pre>
     *
     * <code>REPORT_COMSUME = 2;</code>
     */
    public static final int REPORT_COMSUME_VALUE = 2;


    public final int getNumber() {
      if (this == UNRECOGNIZED) {
        throw new IllegalArgumentException(
            "Can't get the number of an unknown enum value.");
      }
      return value;
    }

    /**
     * @deprecated Use {@link #forNumber(int)} instead.
     */
    @Deprecated
    public static MsgId valueOf(int value) {
      return forNumber(value);
    }

    public static MsgId forNumber(int value) {
      switch (value) {
        case 0: return ENQUE_MESSAGE;
        case 1: return FETCH_MESSAGE;
        case 2: return REPORT_COMSUME;
        default: return null;
      }
    }

    public static com.google.protobuf.Internal.EnumLiteMap<MsgId>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static final com.google.protobuf.Internal.EnumLiteMap<
        MsgId> internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<MsgId>() {
            public MsgId findValueByNumber(int number) {
              return MsgId.forNumber(number);
            }
          };

    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(ordinal());
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return Enums.getDescriptor().getEnumTypes().get(1);
    }

    private static final MsgId[] VALUES = values();

    public static MsgId valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      if (desc.getIndex() == -1) {
        return UNRECOGNIZED;
      }
      return VALUES[desc.getIndex()];
    }

    private final int value;

    private MsgId(int value) {
      this.value = value;
    }

    // @@protoc_insertion_point(enum_scope:MsgId)
  }


  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\022common/Enums.proto*6\n\rConsumeResult\022\020\n" +
      "\014CONSUME_FAIL\020\000\022\023\n\017CONSUME_SUCCESS\020\001*A\n\005" +
      "MsgId\022\021\n\rENQUE_MESSAGE\020\000\022\021\n\rFETCH_MESSAG" +
      "E\020\001\022\022\n\016REPORT_COMSUME\020\002B,\n*com.cookienat" +
      "s.common.papimq.protos.commonb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }

  // @@protoc_insertion_point(outer_class_scope)
}
