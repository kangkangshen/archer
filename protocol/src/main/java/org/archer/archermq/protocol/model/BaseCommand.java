package org.archer.archermq.protocol.model;

public abstract class BaseCommand<RESPONSE> implements Command<RESPONSE> {
    @Override
    public int commandId() {
        return 0;
    }

    @Override
    public int classId() {
        return 0;
    }
}
