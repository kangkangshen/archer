package org.archer.archermq.protocol;


import com.google.common.collect.Maps;
import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.LogConstants;
import org.springframework.util.CollectionUtils;


import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 生命周期基类
 *
 * @author dongyue
 * @date 2020年04月16日18:59:52
 */
public class BaseLifeCycleSupport implements LifeCycle {

    private String currPhase;

    private String currPhaseStatus;

    private Map<String,LifeCycleListener> listeners = Maps.newConcurrentMap();

    @Override
    public String currPhase() {
        return currPhase;
    }

    @Override
    public String currPhaseStatus() {
        return currPhaseStatus;
    }

    @Override
    public void triggerEvent() {
        LifeCycleEvent<BaseLifeCycleSupport> event = new LifeCycleEvent<>();
        event.setPhase(new String(currPhase));
        event.setPhaseStatus(new String(currPhaseStatus));
        event.setTarget(this);
        if(!CollectionUtils.isEmpty(listeners)){
            listeners.forEach((name,listener)->{
                if(listener.interested(event)){
                    listener.responseEvent(event);
                }
            });
        }
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void acceptListener(LifeCycleListener listener) {
        if(Objects.nonNull(listener)){
            listeners.put(listener.name(),listener);
        }
    }

    public String getCurrPhase() {
        return currPhase;
    }

    public void setCurrPhase(String currPhase) {
        this.currPhase = currPhase;
    }

    public String getCurrPhaseStatus() {
        return currPhaseStatus;
    }

    public void setCurrPhaseStatus(String currPhaseStatus) {
        this.currPhaseStatus = currPhaseStatus;
    }

    public Set<String> interestedListenerNames(){
        return listeners.keySet();
    }
}
