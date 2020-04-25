package org.archer.archermq.protocol.transport;

import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.constants.Delimiters;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.protocol.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

/**
 * 标准virtualHost实现
 */
public class StandardVirtualHost extends BaseLifeCycleSupport implements VirtualHost, InitializingBean {

    private Namespace namespace;

    @Value("archermq.virtualhost.name")
    private String name;

    private Registrar<String, Exchange> exchangeRegistry;

    private Registrar<String, MessageQueue> queueRegistry;


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
    public void afterPropertiesSet() throws Exception {
        this.exchangeRegistry = new StandardExchangeRegistry(this);
        this.queueRegistry = new StandardMsgQueueRegistry(this);
    }

    public Registrar<String, Exchange> getExchangeRegistry() {
        return exchangeRegistry;
    }

    @Override
    public Registrar<String, MessageQueue> getMsgQueueRegistry() {
        return queueRegistry;
    }






    public static class StandardExchangeRegistry extends DistributedRegistrar<String, Exchange> {

        private final VirtualHost virtualHost;

        public StandardExchangeRegistry(VirtualHost virtualHost) {
            this.virtualHost = virtualHost;
        }

        public VirtualHost getVirtualHost() {
            return virtualHost;
        }

        @Override
        public boolean contains(String s) {
            return super.contains(virtualHost.name() + Delimiters.UNDERLINE + s);
        }

        @Override
        @Log(layer = LogConstants.TRANSPORT_LAYER)
        public boolean register(String s, Exchange instance) {
            return super.register(virtualHost.name() + Delimiters.UNDERLINE + s, instance);
        }

        @Override
        @Log(layer = LogConstants.TRANSPORT_LAYER)
        public Exchange remove(String s) {
            return super.remove(virtualHost.name() + Delimiters.UNDERLINE + s);
        }

        @Override
        public Exchange get(String s) {
            return super.get(virtualHost.name() + Delimiters.UNDERLINE + s);
        }
    }


    public static class StandardMsgQueueRegistry extends DistributedRegistrar<String, MessageQueue> {
        private final VirtualHost virtualHost;

        public StandardMsgQueueRegistry(VirtualHost virtualHost) {
            this.virtualHost = virtualHost;
        }
        @Override
        public boolean contains(String s) {
            return super.contains(virtualHost.name() + Delimiters.UNDERLINE + s);
        }

        @Override
        @Log(layer = LogConstants.TRANSPORT_LAYER)
        public boolean register(String s, MessageQueue instance) {
            return super.register(virtualHost.name() + Delimiters.UNDERLINE + s, instance);
        }

        @Override
        @Log(layer = LogConstants.TRANSPORT_LAYER)
        public MessageQueue remove(String s) {
            return super.remove(virtualHost.name() + Delimiters.UNDERLINE + s);
        }

        @Override
        public MessageQueue get(String s) {
            return super.get(virtualHost.name() + Delimiters.UNDERLINE + s);
        }

        public VirtualHost getVirtualHost() {
            return virtualHost;
        }
    }

}
