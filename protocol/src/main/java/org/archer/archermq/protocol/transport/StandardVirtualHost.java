package org.archer.archermq.protocol.transport;

import com.google.common.collect.Maps;
import org.archer.archermq.protocol.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 标准virtualHost实现
 */
@Component
public class StandardVirtualHost extends BaseLifeCycleSupport implements VirtualHost {

    @Value("archermq.virtualhost.name")
    private String name;

    private Map<String,Exchange> exchanges = Maps.newConcurrentMap();


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
    public String currPhase() {
        return null;
    }

    @Override
    public String currPhaseStatus() {
        return null;
    }
}
