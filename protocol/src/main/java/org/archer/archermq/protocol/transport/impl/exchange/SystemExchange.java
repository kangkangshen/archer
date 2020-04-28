package org.archer.archermq.protocol.transport.impl.exchange;

import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.MessageQueue;
import org.archer.archermq.protocol.transport.BaseExchange;


/**
 * system交换器类型按如下方式进行工作:
 * 1. 发布者使用路由键S来向交换器发送消息．
 * 2. system交换器将其传递给系统服务S.
 * 系统服务以"amq."开头，为AMQP保留使用. 在服务器环境中，所有其它名称可自由使用. 此交换器类型是可选的.
 *
 * @author dongyue
 * @date 2020年04月28日22:34:18
 */
public class SystemExchange extends BaseExchange {
    public SystemExchange(String name, Integer rawType, String exchangeId, boolean durable) {
        super(name, rawType, exchangeId, durable);
    }

    @Override
    public void acceptMsgQueueBinding(MessageQueue msgQueue, String routingKey) {

    }

    @Override
    public void discardMsgQueueBinding(MessageQueue msgQueue, String routingKey) {

    }

    @Override
    public void exchangeMsg(Message msg, String routingKey) {

    }
}
