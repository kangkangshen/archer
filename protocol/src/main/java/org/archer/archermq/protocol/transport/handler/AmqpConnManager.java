package org.archer.archermq.protocol.transport.handler;

import org.archer.archermq.protocol.LifeCycleEvent;
import org.archer.archermq.protocol.LifeCycleListener;
import org.springframework.stereotype.Component;

/**
 * 用于管理amqp connection,channel注册机
 */
@Component
public class AmqpConnManager implements LifeCycleListener {


    @Override
    public String name() {
        return null;
    }

    @Override
    public String description() {
        return null;
    }

    @Override
    public boolean interested(LifeCycleEvent<?> event) {
        return false;
    }

    @Override
    public void responseEvent(LifeCycleEvent<?> event) {

    }
}
