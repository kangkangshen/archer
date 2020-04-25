package org.archer.archermq.protocol.model;

import org.archer.archermq.common.utils.ApplicationContextHolder;
import org.springframework.context.ApplicationContext;

public abstract class BaseCommand<RESPONSE> implements Command<RESPONSE> {

    private final int classId;

    private final int methodId;

    //不允许直接使用无餐构造器
    private BaseCommand() {
        this.classId = Integer.MIN_VALUE;
        this.methodId = Integer.MIN_VALUE;
    }

    public BaseCommand(int classId, int methodId) {
        this.classId = classId;
        this.methodId = methodId;
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
