package org.archer.archermq.protocol;


import com.google.common.collect.Maps;
import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.springframework.util.Assert;
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
public abstract class BaseLifeCycleSupport implements LifeCycle {

    private String currPhase;

    private String currPhaseStatus;

    private final Map<String, LifeCycleListener> listeners = Maps.newConcurrentMap();

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
        if (!CollectionUtils.isEmpty(listeners)) {
            listeners.forEach((name, listener) -> {
                if (listener.interested(event)) {
                    listener.responseEvent(event);
                }
            });
        }
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void acceptListener(LifeCycleListener listener) {
        if (Objects.nonNull(listener)) {
            listeners.put(listener.name(), listener);
        }
    }

    public String getCurrPhase() {
        return currPhase;
    }

    public void setCurrPhase(String nextPhase) {
        this.currPhase = nextPhase;
    }

    public String getCurrPhaseStatus() {
        return currPhaseStatus;
    }

    public void setCurrPhaseStatus(String nextPhaseStatus) {
        this.currPhaseStatus = nextPhaseStatus;
    }

    public void updateCurrState(String nextPhase, String nextPhaseStatus) {
        Assert.isTrue(couldChangeState(nextPhase, nextPhaseStatus), ExceptionMessages.SystemErrors.STATE_ERR.getDesc());
        setCurrPhase(nextPhase);
        setCurrPhaseStatus(nextPhaseStatus);
    }

    /**
     * 检查当前状态是否允许变更到下一状态
     *
     * @param nextPhase       下一状态的phase
     * @param nextPhaseStatus 下一状态的phaseStatus
     * @return 是否当前当前状态能够流转到下一状态，每一个支持生命周期的都应重写此方法
     */
    protected boolean couldChangeState(String nextPhase, String nextPhaseStatus) {
        return true;
    }


    public Set<String> interestedListenerNames() {
        return listeners.keySet();
    }
}
