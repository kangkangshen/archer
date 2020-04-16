package org.archer.archermq.protocol.transport;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.protocol.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 标准virtualHost实现
 */
@Component
public class StandardVirtualHost extends BaseLifeCycleSupport implements VirtualHost, InitializingBean {

    private Namespace namespace;

    @Value("archermq.virtualhost.name")
    private String name;

    private ExchangeRegistry exchangeRegistry;


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
        return exchangeRegistry.exchanges();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.exchangeRegistry = new StandardExchangeRegistry(this);
    }




    public static class StandardExchangeRegistry implements ExchangeRegistry{

        private final VirtualHost virtualHost;

        private final Map<String,Exchange> exchangeContainer = Maps.newConcurrentMap();

        public StandardExchangeRegistry(VirtualHost virtualHost) {
            this.virtualHost = virtualHost;
        }

        @Override
        public VirtualHost virtualHost() {
            return virtualHost;
        }

        @Override
        @Log(layer = LogConstants.TRANSPORT_LAYER)
        public void registerExchange(Exchange exchange) {
            if(Objects.nonNull(exchange)&& StringUtils.isBlank(exchange.name())){
                exchangeContainer.put(exchange.name(),exchange);
            }
        }

        @Override
        @Log(layer = LogConstants.TRANSPORT_LAYER)
        public void removeExchange(String name) {
            exchangeContainer.remove(name);
        }

        @Override
        public boolean containsExchange(String name) {
            return exchangeContainer.containsKey(name);
        }

        @Override
        public Exchange getExchange(String name) {
            return exchangeContainer.get(name);
        }

        @Override
        public Set<String> exchangeNames() {
            //concurrentHashMap保证每次keySet都是当前keySet的一个副本
            return exchangeContainer.keySet();
        }

        @Override
        public List<Exchange> exchanges() {
            //返回当前exchanges的一份副本
            return Lists.newArrayList(exchangeContainer.values());
        }


    }

}
