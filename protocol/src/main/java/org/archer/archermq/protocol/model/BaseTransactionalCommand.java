package org.archer.archermq.protocol.model;

import org.archer.archermq.protocol.transport.Frame;

/**
 * 支持事务的command命令
 *
 * @author dongyue
 * @date 2020年04月20日18:22:55
 */
public abstract class BaseTransactionalCommand<RESPONSE> extends BaseCommand<RESPONSE>{

    public BaseTransactionalCommand(int classId, int methodId) {
        super(classId, methodId);
    }

    @Override
    public RESPONSE execute() {
        return null;
    }

    protected abstract RESPONSE executeInternal();
}
