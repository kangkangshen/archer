package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.Consumer;

public class StandardConsumer implements Consumer {

    private final String consumerTag;

    private final boolean exclusive;

    private final Channel channel;

    public StandardConsumer(String consumerTag,boolean exclusive, Channel channel) {
        this.consumerTag = consumerTag;
        this.exclusive = exclusive;
        this.channel = channel;
    }

    @Override
    public String id() {
        return consumerTag;
    }

    @Override
    public boolean exclusive() {
        return exclusive;
    }

    @Override
    public Channel channel() {
        return channel;
    }
}
