package org.archer.archermq.protocol.model;

import org.archer.archermq.common.FeatureBased;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 对应amqp的basic类
 *
 * @author dongyue
 * @date 2020年04月20日18:57:49
 */
public final class Basic extends FeatureBased implements Class {

    private static final Logger logger = LoggerFactory.getLogger("");


    @Override
    public int classId() {
        return 60;
    }

    @Override
    public String desc() {
        return "work with basic content";
    }


    /**
     * 设置未被确认消费的个数
     */
    public class Qos implements Command<QosOk> {

        @Override
        public String desc() {
            return "specify quality of service";
        }

        @Override
        public int commandId() {
            return 10;
        }

        @Override
        public QosOk execute() {

        }
    }

    public class QosOk implements Command {

        @Override
        public String desc() {
            return "confirm the requested qos";
        }

        @Override
        public int commandId() {
            return 11;
        }

        @Override
        public void execute() {

        }
    }

    /**
     * 消费消息 (推模式)
     */
    public class Consume extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "start a queue consumer";
        }

        @Override
        public int commandId() {
            return 20;
        }
    }

    public class ConsumeOk implements Command {

        @Override
        public String desc() {
            return "confirm a new consumer";
        }

        @Override
        public int commandId() {
            return 21;
        }

        @Override
        public void execute() {

        }
    }

    /**
     * 取消 todo dongyue
     */
    public class Cancel extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "end a queue consumer";
        }

        @Override
        public int commandId() {
            return 30;
        }
    }

    public class CancelOk implements Command {

        @Override
        public String desc() {
            return "confirm a cancelled consumer";
        }

        @Override
        public int commandId() {
            return 31;
        }

        @Override
        public void execute() {

        }
    }

    /**
     * 发送消息
     */
    public class Publish extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "publish a message";
        }

        @Override
        public int commandId() {
            return 40;
        }
    }

    /**
     * 未能成功路由的消息返回
     */
    public class Return extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "return a failed message";
        }

        @Override
        public int commandId() {
            return 50;
        }
    }

    /**
     * Broker 推送消息
     */
    public class Deliver extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "notify the client of a consumer message";
        }

        @Override
        public int commandId() {
            return 60;
        }
    }

    /**
     * 消费消息 (拉模式)
     */
    public class Get extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "direct access to a queue";
        }

        @Override
        public int commandId() {
            return 70;
        }
    }

    public class GetOk implements Command{

        @Override
        public String desc() {
            return "provide client with a message";
        }

        @Override
        public int commandId() {
            return 71;
        }

        @Override
        public void execute() {

        }
    }

    public class GetEmpty implements Command{

        @Override
        public String desc() {
            return "indicate no messages available";
        }

        @Override
        public int commandId() {
            return 72;
        }

        @Override
        public void execute() {

        }
    }

    /**
     * 确认
     */
    public class Ack implements Command {

        @Override
        public String desc() {
            return "acknowledge one or more messages";
        }

        @Override
        public int commandId() {
            return 80;
        }

        @Override
        public void execute() {

        }
    }

    /**
     * 拒绝(单条消息)
     */
    public class Reject extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return "reject an incoming message";
        }

        @Override
        public int commandId() {
            return 90;
        }
    }

    /**
     * 请求Broker重新发送未被确认的消息
     */
    public class Recover extends BaseTransactionalCommand {

        @Override
        public String desc() {
            return " redeliver unacknowledged messages";
        }

        @Override
        public int commandId() {
            return 110;
        }
    }

    public class RecoverAsync extends BaseTransactionalCommand{

        @Override
        public String desc() {
            return "redeliver unacknowledged messages";
        }

        @Override
        public int commandId() {
            return 100;
        }
    }

    public class RecoverOk implements Command{

        @Override
        public String desc() {
            return "confirm recovery";
        }

        @Override
        public int commandId() {
            return 111;
        }

        @Override
        public void execute() {

        }
    }

}
