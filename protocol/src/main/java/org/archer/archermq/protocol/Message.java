package org.archer.archermq.protocol;


import java.util.Map;

/**
 * 消息定义接口
 */
public interface Message extends LifeCycle {


    /**
     * 消息的key,一般来说，msgKey是具有业务含义的可读值，例如交易单号，实体唯一键等等
     *
     * @return 当前消息的key
     */
    String msgKey();

    /**
     * 消息的ID，用于唯一确定一条消息，例如hash值，uuid等
     *
     * @return 当前消息
     */
    String msgId();

    /**
     * 消息描述，当消息无法投递时，该项十分有用
     *
     * @return 消息描述
     */
    String msgDescription();

    Map<String,Object> msgProperties();

    /**
     * 消息头，里面保存了很多有用的扩展字段
     *
     * @return 消息属性
     */
    MessageHead head();

    /**
     * 消息体，消费者利用其进行消费
     *
     * @return 消息体
     */
    MessageBody body();




}
