package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

/**
 * 对应amqp的exchange类
 *
 * @author dongyue
 * @date 2020年04月20日18:52:39
 */
public final class Exchange extends FeatureBased {


    /*
     * 声明交换器
     */
    public class Declare extends BaseTransactionalCommand {

    }

    /**
     * 删除交换器
     */
    public class Delete extends BaseTransactionalCommand {

    }

    /**
     * 交换器与交换器绑定
     */
    public class Bind extends BaseTransactionalCommand {

    }

    /**
     * 交换器与交换器解绑
     */
    public class UnBind extends BaseTransactionalCommand {

    }


}
