package org.archer.archermq.protocol.transport.impl.virtualhost;

import io.netty.channel.Channel;
import org.archer.archermq.common.register.DistributedRegistrar;
import org.archer.archermq.common.register.Registrar;
import org.archer.archermq.common.register.StandardMemRegistrar;
import org.archer.archermq.protocol.*;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class StandardVirtualHost extends BaseLifeCycleSupport implements VirtualHost {


    private Namespace namespace;

    private String name;

    private Registrar<String, Exchange> exchangeRegistry = new DistributedRegistrar<>();

    private Registrar<String, MessageQueue> msgQueueRegistry = new DistributedRegistrar<>();

    private Registrar<Channel, Connection> connRegistry = new StandardMemRegistrar<>();

    private MessageQueue deadLetteredQueue;

    private ExecutorService taskPool;


    @Override
    public Namespace nameSpace() {
        return namespace;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public List<Exchange> exchanges() {
        return exchangeRegistry.instances();
    }

    @Override
    public MessageQueue deadLetteredQueue() {
        return deadLetteredQueue;
    }

    @Override
    public Registrar<String, Exchange> getExchangeRegistry() {
        return exchangeRegistry;
    }

    @Override
    public Registrar<String, MessageQueue> getMsgQueueRegistry() {
        return msgQueueRegistry;
    }

    @Override
    public Registrar<Channel, Connection> getConnRegistry() {
        return connRegistry;
    }

    @Override
    public ExecutorService taskPool() {
        return taskPool;
    }

    public void setNamespace(Namespace namespace) {
        this.namespace = namespace;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTaskPool(ExecutorService taskPool) {
        this.taskPool = taskPool;
    }
}
