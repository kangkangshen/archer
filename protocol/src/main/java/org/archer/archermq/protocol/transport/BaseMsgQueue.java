package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.BaseLifeCycleSupport;
import org.archer.archermq.protocol.Exchange;
import org.archer.archermq.protocol.MessageQueue;

public class BaseMsgQueue extends BaseLifeCycleSupport implements MessageQueue {

    private final String name;

    private final boolean durable;

    private final boolean exclusive;

    private final boolean autoDelete;

    private String bindingKey;

    public BaseMsgQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
        this.name = name;
        this.durable = durable;
        this.exclusive = exclusive;
        this.autoDelete = autoDelete;
    }

    @Override
    public String bindingKey() {
        return bindingKey;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int msgCount() {
        return 0;
    }

    @Override
    public int consumerCount() {
        return 0;
    }

    @Override
    public boolean durable() {
        return false;
    }


}
