package org.archer.archermq.protocol.transport;

public class StandardMsgQueue extends BaseMsgQueue {



    public StandardMsgQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
        super(name, durable, exclusive, autoDelete);
    }
}
