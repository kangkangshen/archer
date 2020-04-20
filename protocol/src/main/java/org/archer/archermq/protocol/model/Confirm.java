package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

/**
 * 对应amqp的confirm类
 *
 * @author dongyue
 * @date 2020年04月20日19:03:27
 */
public final class Confirm extends FeatureBased {

    /**
     * 开启发送端确认模式
     */
    public class Select extends BaseTransactionalCommand {

    }
}
