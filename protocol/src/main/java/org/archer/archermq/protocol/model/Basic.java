package org.archer.archermq.protocol.model;

import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.FeatureBased;
import org.archer.archermq.common.constants.Delimiters;
import org.archer.archermq.protocol.*;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.Connection;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.transport.ChannelException;
import org.archer.archermq.protocol.transport.ConnectionException;
import org.archer.archermq.protocol.transport.StandardConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

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
     * This method requests a specific quality of service.
     * The QoS can be specified for the current channel or for all channels on the connection.
     * The particular properties and semantics of a qos method always depend on the content class semantics.
     * Though the qos method could in principle apply to both peers, it is currently meaningful only for the server.
     */
    public class Qos extends BaseCommand<QosOk> {

        private int prefetchSize;

        private short prefetchCount;

        private boolean global;

        public Qos(int prefetchSize, short prefetchCount, boolean global) {
            this.prefetchSize = prefetchSize;
            this.prefetchCount = prefetchCount;
            this.global = global;
        }

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

        @Override
        public String desc() {
            return "confirm the requested qos";
        }

        @Override
        public int commandId() {
            return 11;
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

        private String reserved1;

        private String queue;

        private String consumerTag;

        private boolean noLocal;

        private boolean noAck;

        private boolean exclusive;

        private String noWait;

        private Map<String, Object> arguments;

        public Consume(String reserved1, String queue, String consumerTag, boolean noLocal, boolean noAck, boolean exclusive, String noWait, Map<String, Object> arguments) {
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
        public int commandId() {
            return 20;
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
            this.consumerTag = consumerTag;
        }

        @Override
        public String desc() {
            return "confirm a new consumer";
        }

        @Override
        public int commandId() {
            return 21;
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
            this.consumerTag = consumerTag;
            this.noWait = noWait;
        }

        @Override
        public String desc() {
            return "end a queue consumer";
        }

        @Override
        public int commandId() {
            return 30;
        }

        @Override
        protected CancelOk executeInternal() {
            VirtualHost virtualHost = (VirtualHost) getFeature(FeatureKeys.Command.VIRTUALHOST);
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            Registrar<String, MessageQueue> msgQueueRegistry = virtualHost.getMsgQueueRegistry();
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
            this.consumerTag = consumerTag;
        }

        @Override
        public String desc() {
            return "confirm a cancelled consumer";
        }

        @Override
        public int commandId() {
            return 31;
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

        private String reserved1;

        private String exchange;

        private String routingKey;

        private boolean mandatory;

        private boolean immediate;

        public Publish(String reserved1, String exchange, String routingKey, boolean mandatory, boolean immediate) {
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
        public int commandId() {
            return 40;
        }

        @Override
        protected Void executeInternal() {

        }
    }

    /**
     * 未能成功路由的消息返回
     */
    public class Return extends BaseTransactionalCommand<Void> {

        private String replyCode;

        private String replyText;

        private String exchange;

        private String routingKey;

        public Return(String replyCode, String replyText, String exchange, String routingKey) {
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
        public int commandId() {
            return 50;
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

        private String consumerTag;

        private String deliveryTag;

        private boolean redelivered;

        private String exchange;

        private String routingKey;

        public Deliver(String consumerTag, String deliveryTag, boolean redelivered, String exchange, String routingKey) {
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
        public int commandId() {
            return 60;
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

        private String reserved1;

        private String queue;

        private boolean noAck;

        public Get(String reserved1, String queue, boolean noAck) {
            this.reserved1 = reserved1;
            this.queue = queue;
            this.noAck = noAck;
        }

        @Override
        public String desc() {
            return "direct access to a queue";
        }

        @Override
        public int commandId() {
            return 70;
        }

        @Override
        protected GetAck executeInternal() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            Message msg = channel.selectMsg();
            Map<String, Object> msgProperties = msg.msgProperties();
            return new GetOk(
                    msgProperties.get(FeatureKeys.Message.EXCHANGE_NAME),
                    msgProperties.get(FeatureKeys.Message.ROUTING_KEY));
        }
    }

    //标记接口
    public interface GetAck extends Command<Void> {}

    public class GetOk extends BaseCommand<Void> implements GetAck {

        private String deliveryTag;

        private boolean redelivered;

        private String exchange;

        private String routingKey;

        private int msgCnt;

        public GetOk(String deliveryTag, boolean redelivered, String exchange, String routingKey, int msgCnt) {
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
        public int commandId() {
            return 71;
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    public class GetEmpty extends BaseCommand<Void> {

        private String reserved1;

        public GetEmpty(String reserved1) {
            this.reserved1 = reserved1;
        }


        @Override
        public String desc() {
            return "indicate no messages available";
        }

        @Override
        public int commandId() {
            return 72;
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

        private String deliveryTag;

        private boolean multiple;

        public Ack(String deliveryTag, boolean multiple) {
            this.deliveryTag = deliveryTag;
            this.multiple = multiple;
        }

        @Override
        public String desc() {
            return "acknowledge one or more messages";
        }

        @Override
        public int commandId() {
            return 80;
        }

        @Override
        public Void execute() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            if(StringUtils.isBlank(deliveryTag)){
                channel.confirmAllMsg();
            }else{
                channel.confirmMsg(deliveryTag,multiple);
            }
            return null;
        }
    }

    /**
     * 拒绝(单条消息)
     */
    public class Reject extends BaseTransactionalCommand<Void> {

        private String deliveryTag;

        private boolean requeue;


        @Override
        public String desc() {
            return "reject an incoming message";
        }

        @Override
        public int commandId() {
            return 90;
        }

        @Override
        protected Void executeInternal() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            if(requeue){
                throw new ChannelException(ExceptionMessages.ConnectionErrors.NOT_IMPLEMENTED);
            }else{
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
            this.requeue = requeue;
        }

        @Override
        public String desc() {
            return " redeliver unacknowledged messages";
        }

        @Override
        public int commandId() {
            return 110;
        }

        @Override
        protected RecoverOk executeInternal() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            Queue<Message> unConfirmedMsg = channel.unConfirmedMsg();
            if(requeue){
                for(Message msg:unConfirmedMsg){
                    channel.exchange(msg);
                }
            }else{
                for(Message msg:unConfirmedMsg){
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
            this.requeue = requeue;
        }

        @Override
        public String desc() {
            return "redeliver unacknowledged messages";
        }

        @Override
        public int commandId() {
            return 100;
        }

        @Override
        protected RecoverOk executeInternal() {
            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);
            Queue<Message> unConfirmedMsg = channel.unConfirmedMsg();
            if(requeue){
                for(Message msg:unConfirmedMsg){
                    channel.exchange(msg);
                }
            }else{
                for(Message msg:unConfirmedMsg){
                    channel.redeliver(msg);
                }
            }
            return new RecoverOk();
        }
    }

    public class RecoverOk extends BaseCommand<Void> {

        @Override
        public String desc() {
            return "confirm recovery";
        }

        @Override
        public int commandId() {
            return 111;
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

}
