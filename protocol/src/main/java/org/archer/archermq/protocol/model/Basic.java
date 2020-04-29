package org.archer.archermq.protocol.model;

import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.FeatureBased;
import org.archer.archermq.common.constants.Delimiters;
import org.archer.archermq.protocol.register.Registrar;
import org.archer.archermq.common.utils.ApplicationContextHolder;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.Connection;
import org.archer.archermq.protocol.*;
import org.archer.archermq.protocol.constants.DeliverMode;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.transport.ChannelException;
import org.archer.archermq.protocol.transport.ConnectionException;
import org.archer.archermq.protocol.transport.StandardConsumer;
import org.archer.archermq.protocol.transport.bootstrap.PublishTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 对应amqp的basic类
 *
 * @author dongyue
 * @date 2020年04月20日18:57:49
 */
public final class Basic extends FeatureBased implements Class {

    private static final Logger logger = LoggerFactory.getLogger("");
    private static final int classId = 60;

    @Override
    public int classId() {
        return classId;
    }

    @Override
    public String desc() {
        return "work with basic content";
    }


    /**
     * This method requests a specific quality of service.
     * The QoS can be specified for the current channel or for all channels on the connection.
     * The particular properties and semantics of a qos method always depend on the content class semantics.
     * Though the qos method could in principle apply to both peers, it is currently meaningful only for the server.
     */
    public class Qos extends BaseCommand<QosOk> {

        private final int prefetchSize;

        private final short prefetchCount;

        private final boolean global;

        public Qos(int prefetchSize, short prefetchCount, boolean global) {
            super(classId, 10);
            this.prefetchSize = prefetchSize;
            this.prefetchCount = prefetchCount;
            this.global = global;
        }

        @Override
        public String desc() {
            return "specify quality of service";
        }


        @Override
        public QosOk execute() {
            if (global) {
                Connection connection = (Connection) getFeature(FeatureKeys.Command.AMQP_CONNECTION);
                connection.qos(prefetchSize, prefetchCount);
            } else {
                Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
                channel.qos(prefetchSize, prefetchCount);
            }
            return new QosOk();
        }
    }

    public class QosOk extends BaseCommand<Void> {

        public QosOk() {
            super(classId, 11);
        }

        @Override
        public String desc() {
            return "confirm the requested qos";
        }


        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    /**
     * 消费消息 (推模式)
     */
    public class Consume extends BaseTransactionalCommand<ConsumeOk> {

        private final String reserved1;

        private final String queue;

        private final String consumerTag;

        private final boolean noLocal;

        private final boolean noAck;

        private final boolean exclusive;

        private final String noWait;

        private final Map<String, Object> arguments;

        public Consume(String reserved1, String queue, String consumerTag, boolean noLocal, boolean noAck, boolean exclusive, String noWait, Map<String, Object> arguments) {
            super(classId, 20);
            this.reserved1 = reserved1;
            this.queue = queue;
            this.consumerTag = consumerTag;
            this.noLocal = noLocal;
            this.noAck = noAck;
            this.exclusive = exclusive;
            this.noWait = noWait;
            this.arguments = arguments;
        }

        @Override
        public String desc() {
            return "start a queue consumer";
        }

        @Override
        protected ConsumeOk executeInternal() {
            VirtualHost virtualHost = (VirtualHost) getFeature(FeatureKeys.Command.VIRTUALHOST);
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            Registrar<String, MessageQueue> msgQueueRegistry = virtualHost.getMsgQueueRegistry();
            if (!msgQueueRegistry.contains(queue)) {
                throw new ChannelException(ExceptionMessages.ChannelErrors.NOT_FOUND);
            }
            MessageQueue msgQueue = msgQueueRegistry.get(queue);
            Registrar<String/*channel.id()+ Delimiters.UNDERLINE+consumerTag*/, Consumer> consumerRegistry = msgQueue.getConsumerRegistry();
            if (exclusive) {
                if (!msgQueue.exclusive()) {
                    throw new ChannelException(ExceptionMessages.ChannelErrors.EXCLUSIVE);
                } else {
                    if (consumerRegistry.ids().isEmpty()) {
                        Consumer newConsumer = new StandardConsumer(consumerTag, exclusive, channel);
                        consumerRegistry.register(channel.id() + Delimiters.UNDERLINE + consumerTag, newConsumer);
                        return new ConsumeOk(consumerTag);
                    } else if (consumerRegistry.contains(channel.id() + Delimiters.UNDERLINE + consumerTag)) {
                        return new ConsumeOk(consumerTag);
                    } else {
                        throw new ChannelException(ExceptionMessages.ChannelErrors.EXCLUSIVE);
                    }
                }
            } else {
                Consumer newConsumer = new StandardConsumer(consumerTag, exclusive, channel);
                consumerRegistry.register(channel.id() + Delimiters.UNDERLINE + consumerTag, newConsumer);
                return new ConsumeOk(consumerTag);
            }
        }
    }

    public class ConsumeOk extends BaseCommand<Void> {

        private final String consumerTag;

        public ConsumeOk(String consumerTag) {
            super(classId, 21);
            this.consumerTag = consumerTag;
        }

        @Override
        public String desc() {
            return "confirm a new consumer";
        }


        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }


        public String getConsumerTag() {
            return consumerTag;
        }
    }

    /**
     * This method cancels a consumer.
     * This does not affect already delivered messages, but it does mean the server will not send any more messages for that consumer.
     * The client may receive an arbitrary number of messages in between sending the cancel method and receiving the cancel­ok reply.
     */
    public class Cancel extends BaseTransactionalCommand<CancelOk> {

        private final String consumerTag;

        private final String noWait;


        public Cancel(String consumerTag, String noWait) {
            super(classId, 30);
            this.consumerTag = consumerTag;
            this.noWait = noWait;
        }

        @Override
        public String desc() {
            return "end a queue consumer";
        }


        @Override
        protected CancelOk executeInternal() {
            VirtualHost virtualHost = (VirtualHost) getFeature(FeatureKeys.Command.VIRTUALHOST);
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            Set<MessageQueue> consuming = channel.consuming();
            for (MessageQueue consumingQueue : consuming) {
                Registrar<String, Consumer> consumerRegistry = consumingQueue.getConsumerRegistry();
                consumerRegistry.remove(channel.id() + Delimiters.UNDERLINE + consumerTag);
            }
            return new CancelOk(consumerTag);
        }
    }

    public class CancelOk extends BaseCommand<Void> {

        private final String consumerTag;

        public CancelOk(String consumerTag) {
            super(classId, 31);
            this.consumerTag = consumerTag;
        }

        @Override
        public String desc() {
            return "confirm a cancelled consumer";
        }


        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }

        public String getConsumerTag() {
            return consumerTag;
        }
    }

    /**
     * 发送消息
     * 该命令是一个带有上下文调用关系的命令，当接收到Publish命令之后，不立即触发命令执行
     * 当再接收到内容帧时，触发该命令
     */
    public class Publish extends BaseTransactionalCommand<Void> {

        private final String reserved1;

        private final String exchange;

        private final String routingKey;

        private final boolean mandatory;

        private final boolean immediate;

        public Publish(String reserved1, String exchange, String routingKey, boolean mandatory, boolean immediate) {
            super(classId, 40);
            this.reserved1 = reserved1;
            this.exchange = exchange;
            this.routingKey = routingKey;
            this.mandatory = mandatory;
            this.immediate = immediate;
        }

        @Override
        public String desc() {
            return "publish a message";
        }


        @Override
        protected Void executeInternal() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            channel.setPublishTag(new PublishTag(exchange,routingKey,mandatory,immediate));
            return null;
        }
    }

    /**
     * 未能成功路由的消息返回
     */
    public class Return extends BaseTransactionalCommand<Void> {

        private final String replyCode;

        private final String replyText;

        private final String exchange;

        private final String routingKey;

        public Return(String replyCode, String replyText, String exchange, String routingKey) {
            super(classId, 50);
            this.replyCode = replyCode;
            this.replyText = replyText;
            this.exchange = exchange;
            this.routingKey = routingKey;
        }

        @Override
        public String desc() {
            return "return a failed message";
        }


        @Override
        protected Void executeInternal() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    /**
     * Broker 推送消息
     */
    public class Deliver extends BaseTransactionalCommand<Void> {

        private final String consumerTag;

        private final String deliveryTag;

        private final boolean redelivered;

        private final String exchange;

        private final String routingKey;

        public Deliver(String consumerTag, String deliveryTag, boolean redelivered, String exchange, String routingKey) {
            super(classId, 60);
            this.consumerTag = consumerTag;
            this.deliveryTag = deliveryTag;
            this.redelivered = redelivered;
            this.exchange = exchange;
            this.routingKey = routingKey;
        }

        @Override
        public String desc() {
            return "notify the client of a consumer message";
        }


        @Override
        protected Void executeInternal() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    /**
     * 消费消息 (拉模式)
     */
    public class Get extends BaseTransactionalCommand<GetAck> {

        private final String reserved1;

        private final String queue;

        private final boolean noAck;

        public Get(String reserved1, String queue, boolean noAck) {
            super(classId, 70);
            this.reserved1 = reserved1;
            this.queue = queue;
            this.noAck = noAck;
        }

        @Override
        public String desc() {
            return "direct access to a queue";
        }

        @Override
        protected GetAck executeInternal() {
            VirtualHost virtualHost = (VirtualHost) getFeature(FeatureKeys.Command.VIRTUALHOST);
            Registrar<String, MessageQueue> msgQueueRegistry = virtualHost.getMsgQueueRegistry();
            if (!msgQueueRegistry.contains(queue)) {
                MessageQueue msgQueue = msgQueueRegistry.get(queue);
                msgQueue.setDeliverMode(DeliverMode.PULL);
                if (msgQueue.localMsgCnt() == 0) {
                    return new GetEmpty(reserved1);
                } else {
                    //TODO dongyue
                    return new GetOk(null, false, null, null, 0);
                }
            }
            //TODO dongyue
            return new GetOk(null, false, null, null, 0);

        }
    }

    //标记接口
    public interface GetAck extends Command<Void> {
    }

    public class GetOk extends BaseCommand<Void> implements GetAck {

        private final String deliveryTag;

        private final boolean redelivered;

        private final String exchange;

        private final String routingKey;

        private final int msgCnt;

        public GetOk(String deliveryTag, boolean redelivered, String exchange, String routingKey, int msgCnt) {
            super(classId, 71);
            this.deliveryTag = deliveryTag;
            this.redelivered = redelivered;
            this.exchange = exchange;
            this.routingKey = routingKey;
            this.msgCnt = msgCnt;
        }

        @Override
        public String desc() {
            return "provide client with a message";
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    public class GetEmpty extends BaseCommand<Void> implements GetAck {

        private final String reserved1;

        public GetEmpty(String reserved1) {
            super(classId, 72);
            this.reserved1 = reserved1;
        }


        @Override
        public String desc() {
            return "indicate no messages available";
        }


        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    /**
     * 确认
     */
    public class Ack extends BaseCommand<Void> {

        private final String deliveryTag;

        private final boolean multiple;

        public Ack(String deliveryTag, boolean multiple) {
            super(classId, 80);
            this.deliveryTag = deliveryTag;
            this.multiple = multiple;
        }

        @Override
        public String desc() {
            return "acknowledge one or more messages";
        }

        @Override
        public Void execute() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            if (StringUtils.isBlank(deliveryTag)) {
                channel.confirmAllMsg();
            } else {
                channel.confirmMsg(deliveryTag, multiple);
            }
            return null;
        }
    }

    /**
     * 拒绝(单条消息)
     */
    public class Reject extends BaseTransactionalCommand<Void> {

        private final String deliveryTag;

        private final boolean requeue;

        public Reject(String deliveryTag, boolean requeue) {
            super(classId, 90);
            this.deliveryTag = deliveryTag;
            this.requeue = requeue;
        }


        @Override
        public String desc() {
            return "reject an incoming message";
        }


        @Override
        protected Void executeInternal() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            if (requeue) {
                throw new ChannelException(ExceptionMessages.ConnectionErrors.NOT_IMPLEMENTED);
            } else {
                //TODO dongyue
                return null;
            }
        }
    }

    /**
     * 请求Broker重新发送未被确认的消息
     */
    public class Recover extends BaseTransactionalCommand<RecoverOk> {

        private final boolean requeue;

        public Recover(boolean requeue) {
            super(classId, 110);
            this.requeue = requeue;
        }

        @Override
        public String desc() {
            return " redeliver unacknowledged messages";
        }

        @Override
        protected RecoverOk executeInternal() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            List<Message> unConfirmedMsg = channel.unConfirmedMsg();
            if (requeue) {
                for (Message msg : unConfirmedMsg) {
                    channel.exchange(msg);
                }
            } else {
                for (Message msg : unConfirmedMsg) {
                    msg.msgProperties().put(FeatureKeys.Message.REPUBLISH,Boolean.TRUE);
                    channel.redeliver(msg);
                }
            }
            return new RecoverOk();
        }

        public boolean isRequeue() {
            return requeue;
        }
    }

    public class RecoverAsync extends BaseTransactionalCommand<RecoverOk> {

        private final boolean requeue;

        public RecoverAsync(boolean requeue) {
            super(classId, 100);
            this.requeue = requeue;
        }

        @Override
        public String desc() {
            return "redeliver unacknowledged messages";
        }

        @Override
        protected RecoverOk executeInternal() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            List<Message> unConfirmedMsg = channel.unConfirmedMsg();
            if (requeue) {
                for (Message msg : unConfirmedMsg) {
                    channel.exchange(msg);
                }
            } else {
                for (Message msg : unConfirmedMsg) {
                    channel.redeliver(msg);
                }
            }
            return new RecoverOk();
        }
    }

    public class RecoverOk extends BaseCommand<Void> {

        public RecoverOk() {
            super(classId, 111);
        }

        @Override
        public String desc() {
            return "confirm recovery";
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    static {
        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        MethodResolver methodResolver = context.getBean(MethodResolver.class);
        methodResolver.register(classId, 111, RecoverOk.class);
        methodResolver.register(classId, 100, RecoverAsync.class);
        methodResolver.register(classId, 110, Recover.class);
        methodResolver.register(classId, 90, Reject.class);
        methodResolver.register(classId, 80, Ack.class);
        methodResolver.register(classId, 72, GetEmpty.class);
        methodResolver.register(classId, 71, GetOk.class);
        methodResolver.register(classId, 70, Get.class);
        methodResolver.register(classId, 60, Deliver.class);
        methodResolver.register(classId, 50, Return.class);
        methodResolver.register(classId, 40, Publish.class);
        methodResolver.register(classId, 31, CancelOk.class);
        methodResolver.register(classId, 30, Cancel.class);
        methodResolver.register(classId, 21, ConsumeOk.class);
        methodResolver.register(classId, 20, Consume.class);
        methodResolver.register(classId, 11, QosOk.class);
        methodResolver.register(classId, 10, Qos.class);


    }

}
