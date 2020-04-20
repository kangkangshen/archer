package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

/**
 * 对应于amqp的queue类
 *
 * @author dongyue
 * @date 2020年04月20日18:55:40
 */
public final class Queue extends FeatureBased {

    /**
     * 声明队列
     */
    public class Declare extends BaseTransactionalCommand {

    }

    /**
     * 队列与交换器绑定
     */
    public class Bind extends BaseTransactionalCommand {

    }

    /*
     * 队列与交换器解绑
     */
    public class Unbind extends BaseTransactionalCommand {

    }

    /**
     * 清除队列中的内容
     */
    public class Purge extends BaseTransactionalCommand {

    }

    /**
     * 删除队列
     */
    public class Delete extends BaseTransactionalCommand {

    }
}
