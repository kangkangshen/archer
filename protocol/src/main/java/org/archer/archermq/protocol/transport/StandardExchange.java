package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.MessageQueue;
import org.archer.archermq.protocol.constants.ExchangeTypeEnum;

public class StandardExchange extends BaseExchange{


    public StandardExchange(String name, Integer rawType, String exchangeId,boolean durable) {
        super(name, rawType, exchangeId,durable);
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
