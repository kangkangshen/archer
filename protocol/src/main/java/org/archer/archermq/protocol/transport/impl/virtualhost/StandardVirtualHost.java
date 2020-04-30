package org.archer.archermq.protocol.transport.impl.virtualhost;

import io.netty.channel.Channel;
import org.archer.archermq.common.Namespace;
import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.archer.archermq.config.register.Metadata;
import org.archer.archermq.config.register.Registrar;
import org.archer.archermq.config.register.StandardMemRegistrar;
import org.archer.archermq.protocol.*;
import org.archer.archermq.config.register.StandardMetaRegistrar;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * 标准virtualHost实现
 */
public class StandardVirtualHost extends BaseLifeCycleSupport implements VirtualHost, InitializingBean {

    private Namespace namespace;

    @Value("archermq.virtualhost.name")
    private String name;

    private Registrar<String, Exchange> exchangeRegistry;

    private Registrar<String, MessageQueue> queueRegistry;

    private MessageQueue deadLetteredQueue;

    private Registrar<Channel, Connection> connectionRegistry;

    private ExecutorService executorService;


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
    public void afterPropertiesSet() throws Exception {
        this.exchangeRegistry = new StandardExchangeRegistry(this);
        this.queueRegistry = new StandardMsgQueueRegistry(this);
        this.connectionRegistry = new StandardMemRegistrar<>();

    }

    public Registrar<String, Exchange> getExchangeRegistry() {
        return exchangeRegistry;
    }

    @Override
    public Registrar<String, MessageQueue> getMsgQueueRegistry() {
        return queueRegistry;
    }

    @Override
    public Registrar<Channel, Connection> getConnRegistry() {
        return connectionRegistry;
    }

    @Override
    public ExecutorService taskPool() {
        return executorService;
    }


    public static class StandardExchangeRegistry implements Registrar<String, Exchange> {

        private final VirtualHost virtualHost;

        private StandardMetaRegistrar metaRegistrar;

        private StandardMemRegistrar<String, Exchange> memRegistrar;

        public StandardExchangeRegistry(VirtualHost virtualHost) {
            this.virtualHost = virtualHost;
        }

        public VirtualHost getVirtualHost() {
            return virtualHost;
        }

        @Override
        public boolean contains(String s) {
            return memRegistrar.contains(s);
        }

        @Override
        @Log(layer = LogConstants.TRANSPORT_LAYER)
        public boolean register(String s, Exchange instance) {
            Metadata exchangeMetadata = instance.meta();
            exchangeMetadata.setVirtualHost(virtualHost.name());
            boolean success = memRegistrar.register(s, instance);
            if (instance.isDurable()) {
                success &= metaRegistrar.register(s, instance.meta());
            }
            instance.updateCurrState(LifeCyclePhases.Exchange.REGISTERED, LifeCyclePhases.Status.FINISH);
            instance.triggerEvent();
            return success;
        }

        @Override
        @Log(layer = LogConstants.TRANSPORT_LAYER)
        public Exchange remove(String s) {
            Exchange result = memRegistrar.remove(s);
            metaRegistrar.remove(s);
            result.updateCurrState(LifeCyclePhases.Exchange.DELETE, LifeCyclePhases.Status.FINISH);
            result.triggerEvent();
            return result;
        }

        @Override
        public Exchange get(String s) {
            return memRegistrar.get(s);
        }

        @Override
        public Set<String> ids() {
            return memRegistrar.ids();
        }

        @Override
        public List<Exchange> instances() {
            return memRegistrar.instances();
        }

        @Override
        public int size() {
            return memRegistrar.size();
        }
    }


    public static class StandardMsgQueueRegistry implements Registrar<String, MessageQueue> {

        private final VirtualHost virtualHost;

        private StandardMetaRegistrar metaRegistrar;

        private StandardMemRegistrar<String, MessageQueue> memRegistrar;


        public StandardMsgQueueRegistry(VirtualHost virtualHost) {
            this.virtualHost = virtualHost;
        }

        @Override
        public boolean contains(String s) {
            return memRegistrar.contains(s);
        }

        @Override
        @Log(layer = LogConstants.TRANSPORT_LAYER)
        public boolean register(String s, MessageQueue instance) {
            Metadata exchangeMetadata = instance.meta();
            exchangeMetadata.setVirtualHost(virtualHost.name());
            boolean success = memRegistrar.register(s, instance);
            if (instance.durable()) {
                success &= metaRegistrar.register(s, instance.meta());
            }
            instance.updateCurrState(LifeCyclePhases.Exchange.REGISTERED, LifeCyclePhases.Status.FINISH);
            instance.triggerEvent();
            return success;
        }

        @Override
        @Log(layer = LogConstants.TRANSPORT_LAYER)
        public MessageQueue remove(String s) {
            MessageQueue result = memRegistrar.remove(s);
            metaRegistrar.remove(s);
            result.updateCurrState(LifeCyclePhases.Exchange.DELETE, LifeCyclePhases.Status.FINISH);
            result.triggerEvent();
            return result;
        }

        @Override
        public MessageQueue get(String s) {
            return memRegistrar.get(s);
        }

        @Override
        public Set<String> ids() {
            return memRegistrar.ids();
        }

        @Override
        public List<MessageQueue> instances() {
            return memRegistrar.instances();
        }

        @Override
        public int size() {
            return memRegistrar.size();
        }

        public VirtualHost getVirtualHost() {
            return virtualHost;
        }
    }

}
