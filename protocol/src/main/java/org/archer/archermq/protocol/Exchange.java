package org.archer.archermq.protocol;


import org.archer.archermq.protocol.constants.ExchangeTypeEnum;

/**
 * 服务器中的实体，用来接收生产者发送的消息并将这些消息路由给服务器中的队列。
 * 交换器是虚拟主机中的路由代理。
 * 交换器实例（俗称“一个交换器”）接受消息和路由信息（路由关键字），然后将消息发送给消息队列或者是一些AMQP服务器厂商扩展的内部服务。对于每个虚拟主机内部，交换器有独一无二的名字。
 * 其生命周期设计如下：
 * 1. client 请求server确保交换器是否存在(Declare). client可细化到,"如果交换器不存在则进行创建",或 ＂如果交换器不存在，警告我，不需要创建".
 * 2. client发布消息到交换器.
 * 3. client可选择删除交换器(Delete).
 *
 * @author dongyue
 * @date 2020年04月13日18:38:47
 */
public interface Exchange extends LifeCycle {

    /**
     * 标准交换器实例前辍是"amq."
     */
    String STANDARD_EXCHANGE_NAME_PREFIX = "amq.";

    /**
     * 用户定义的交换器类型前辍必须是"x-"
     */
    String CUSTOM_EXCHANGE_NAME_PREFIX = "x-";

    String name();


    /**
     * 获取当前的交换器的类型
     *
     * @return 当前交换器的类型
     * @see ExchangeTypeEnum
     */
    ExchangeTypeEnum type();

    /**
     * 获取当前交换器的唯一编号
     *
     * @return 当前交换器的唯一编号
     */
    String exchangeId();

    void acceptMsgQueueBinding(MessageQueue msgQueue,String routingKey);

    void discardMsgQueueBinding(MessageQueue msgQueue,String routingKey);

    void exchangeMsg(Message msg,String routingKey);



}
