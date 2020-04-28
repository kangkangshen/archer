package org.archer.archermq.protocol.transport.bootstrap;

import lombok.Data;

@Data
public class PublishTag {

    private final String exchange;

    private final String routingKey;

    private final boolean mandatory;

    private final boolean immediate;

    public PublishTag(String exchange, String routingKey, boolean mandatory, boolean immediate) {
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.mandatory = mandatory;
        this.immediate = immediate;
    }


}
