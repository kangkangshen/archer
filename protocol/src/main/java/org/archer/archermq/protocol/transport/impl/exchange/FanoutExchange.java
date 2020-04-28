package org.archer.archermq.protocol.transport.impl.exchange;

import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.MessageQueue;
import org.archer.archermq.protocol.constants.ExchangeTypeEnum;
import org.archer.archermq.protocol.transport.BaseExchange;


/**
 * fanout交换器类型按如下方式来工作:
 * 1. 消息队列不使用参数来绑定交换器.
 * 2. 发布者向交换器发送消息.
 * 3. 消息无条件传递给消息队列。
 * fanout 交换器是微不足道的设计与实现．此交换器类型和预声明的交换器称为amq.fanout,它是强制的.
 *
 * @author dongyue
 * @date 2020年04月28日22:32:52
 */
public class FanoutExchange extends BaseExchange {
    public FanoutExchange(String name, String exchangeId, boolean durable) {
        super(name, ExchangeTypeEnum.FANOUT.getVal(), exchangeId, durable);
    }

    @Override
    protected void exchangeMsgInternal(Message message, String routingKey) {

    }
}
