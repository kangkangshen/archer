package org.archer.archermq.protocol;

/**
 * 生命周期定义
 *
 * @author dongyue
 * @date 2020年04月13日19:17:43
 */
public interface LifeCycle {

    /**
     * 当前生命周期所属阶段，例如创建，销毁，运行中等等阶段，使用Integer加以标识
     *
     * @return 当前生命周期所属阶段
     */
    String currPhase();

    /**
     * 当前生命周期所属阶段的状态，例如开始，结束，异常
     *
     * @return 当前生命周期所属阶段的状态
     */
    String currPhaseStatus();

    /**
     * 触发当前的生命周期事件,该事件将会由对应的事件监听者所截获
     *
     */
    void triggerEvent();

    void acceptListener(LifeCycleListener listener);


    void updateCurrState(String nextPhase, String nextPhaseStatus);

}
