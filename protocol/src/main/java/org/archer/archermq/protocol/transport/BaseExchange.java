package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.BaseLifeCycleSupport;
import org.archer.archermq.protocol.Exchange;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.ExchangeTypeEnum;
import org.springframework.util.Assert;

public abstract class BaseExchange extends BaseLifeCycleSupport implements Exchange {

    private final String name;

    private final ExchangeTypeEnum exchangeType;

    private final String exchangeId;

    private final boolean durable;

    public BaseExchange(String name, Integer rawType) {
        this(name,rawType,name,false);
    }

    public BaseExchange(String name, Integer rawType, String exchangeId,boolean durable) {
        this.name = name;
        this.exchangeId = exchangeId;
        this.durable = durable;
        ExchangeTypeEnum exchangeType = ExchangeTypeEnum.getByVal(rawType);
        Assert.notNull(exchangeType, ExceptionMessages.buildExceptionMsgWithTemplate("exchangeType # not support or wrong.", String.valueOf(rawType)));
        this.exchangeType = exchangeType;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ExchangeTypeEnum type() {
        return exchangeType;
    }

    @Override
    public String exchangeId() {
        return exchangeId;
    }

    public boolean isDurable() {
        return durable;
    }
}
