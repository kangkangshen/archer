package org.archer.archermq.protocol.model;

import org.archer.archermq.common.utils.ApplicationContextHolder;
import org.springframework.context.ApplicationContext;

public abstract class BaseCommand<RESPONSE> implements Command<RESPONSE> {

    private static final MethodResolver methodResolver;

    private final int classId;

    private final int methodId;

    public BaseCommand(int classId, int methodId) {
        this.classId = classId;
        this.methodId = methodId;
        methodResolver.register(classId,methodId);
    }


    @Override
    public int commandId() {
        return methodId;
    }

    @Override
    public int classId() {
        return classId;
    }


}
