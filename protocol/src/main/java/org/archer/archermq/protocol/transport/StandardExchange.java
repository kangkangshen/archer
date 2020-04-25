package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.constants.ExchangeTypeEnum;

public class StandardExchange extends BaseExchange{


    public StandardExchange(String name, Integer rawType, String exchangeId,boolean durable) {
        super(name, rawType, exchangeId,durable);
    }
}
