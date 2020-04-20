package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;

/**
 * 对应amqp的basic类
 *
 * @author dongyue
 * @date 2020年04月20日18:57:49
 */
public final class Basic extends FeatureBased {

    /**
     * 设置未被确认消费的个数
     */
    public class Qos implements Command {

        @Override
        public void execute() {

        }
    }

    /**
     * 消费消息 (推模式)
     */
    public class Consume extends BaseTransactionalCommand {

    }

    /**
     * 取消 todo dongyue
     */
    public class Cancel extends BaseTransactionalCommand {

    }

    /**
     * 发送消息
     */
    public class Publish extends BaseTransactionalCommand {

    }

    /**
     * 未能成功路由的消息返回
     */
    public class Return extends BaseTransactionalCommand {

    }

    /**
     * Broker 推送消息
     */
    public class Deliver extends BaseTransactionalCommand {

    }

    /**
     * 消费消息 (拉模式)
     */
    public class Get extends BaseTransactionalCommand {

    }

    /**
     * 确认
     */
    public class Ack implements Command {

        @Override
        public void execute() {

        }
    }

    /**
     * 拒绝(单条消息)
     */
    public class Reject extends BaseTransactionalCommand {

    }

    /**
     * 请求Broker重新发送未被确认的消息
     */
    public class Recover extends BaseTransactionalCommand {

    }

    /**
     * 拒绝 (可批量拒绝)
     */
    public class Nack extends BaseTransactionalCommand {

    }
}
