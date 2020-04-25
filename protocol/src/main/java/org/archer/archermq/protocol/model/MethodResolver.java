package org.archer.archermq.protocol.model;

import org.archer.archermq.protocol.transport.StandardMethodFrame;

public interface MethodResolver {

    boolean support(int classId,int methodId);

    void register(int classId, int methodId, java.lang.Class<? extends Command<?>> commandClass);

    Command<?> route(StandardMethodFrame methodFrame);

//    Object invoke(StandardMethodFrame methodFrame);

}
