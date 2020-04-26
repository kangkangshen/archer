package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.BaseLifeCycleSupport;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.Connection;

import java.util.List;

public class StandardAmqpConnection extends BaseLifeCycleSupport implements Connection {



    @Override
    public Channel openChannel() {
        return null;
    }

    @Override
    public void qos(int prefetchSize, short prefetchCount) {

    }

    @Override
    public List<Channel> channels() {
        return null;
    }
}
