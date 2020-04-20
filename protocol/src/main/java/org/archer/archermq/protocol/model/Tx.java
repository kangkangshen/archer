package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

/**
 * 对应amqp的tx类
 */
public final class Tx extends FeatureBased {

    /**
     * 开启事务
     */
    public class Select extends BaseTransactionalCommand {

    }

    /**
     * 事务提交
     */
    public class Commit extends BaseTransactionalCommand {

    }

    /**
     * 事务回滚
     */
    public class Rollback extends BaseTransactionalCommand {

    }
}
