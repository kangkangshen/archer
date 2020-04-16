package org.archer.archermq.protocol;

public interface LifeCycleListener {

    String name();

    String description();

    boolean interested(LifeCycleEvent<?> event);

    void responseEvent(LifeCycleEvent<?> event);

}
