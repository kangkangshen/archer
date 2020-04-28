package org.archer.archermq.protocol.transport.impl.virtualhost;

import io.netty.channel.Channel;
import org.archer.archermq.common.register.Registrar;
import org.archer.archermq.protocol.*;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class StandardVirtualHost implements VirtualHost {


    @Override
    public Namespace nameSpace() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public List<Exchange> exchanges() {
        return null;
    }

    @Override
    public MessageQueue deadLetteredQueue() {
        return null;
    }

    @Override
    public Registrar<String, Exchange> getExchangeRegistry() {
        return null;
    }

    @Override
    public Registrar<String, MessageQueue> getMsgQueueRegistry() {
        return null;
    }

    @Override
    public Registrar<Channel, Connection> getConnRegistry() {
        return null;
    }

    @Override
    public ExecutorService taskPool() {
        return null;
    }

    @Override
    public String currPhase() {
        return null;
    }

    @Override
    public String currPhaseStatus() {
        return null;
    }

    @Override
    public void triggerEvent() {

    }

    @Override
    public void acceptListener(LifeCycleListener listener) {

    }

    @Override
    public void updateCurrState(String nextPhase, String nextPhaseStatus) {

    }
}
