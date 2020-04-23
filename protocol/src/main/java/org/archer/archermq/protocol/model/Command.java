package org.archer.archermq.protocol.model;

import org.archer.archermq.protocol.transport.Frame;

/**
 * 基础命令接口
 *
 * @author dongyue
 * @date 2020年04月20日18:18:16
 */
public interface Command<RESPONSE> {

    default void redo() {
        throw new UnsupportedOperationException(this.getClass().getName() + " dont support redo");
    }

    default void undo() {
        throw new UnsupportedOperationException(this.getClass().getName() + " dont support undo");
    }

    String desc();

    int commandId();

    RESPONSE execute();
}
