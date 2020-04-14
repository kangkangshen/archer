package org.archer.archermq.protocol;


import org.archer.archermq.protocol.constants.ExchangeTypeEnum;

/**
 * 服务器中的实体，用来接收生产者发送的消息并将这些消息路由给服务器中的队列。
 * 交换器是虚拟主机中的路由代理。
 * 交换器实例（俗称“一个交换器”）接受消息和路由信息（路由关键字），然后将消息发送给消息队列或者是一些AMQP服务器厂商扩展的内部服务。对于每个虚拟主机内部，交换器有独一无二的名字。
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


}
