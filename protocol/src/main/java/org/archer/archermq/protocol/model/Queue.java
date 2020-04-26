package org.archer.archermq.protocol.model;

import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.FeatureBased;
import org.archer.archermq.common.utils.ApplicationContextHolder;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.Exchange;
import org.archer.archermq.protocol.*;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.archer.archermq.protocol.transport.ChannelException;
import org.archer.archermq.protocol.transport.ConnectionException;
import org.archer.archermq.protocol.transport.StandardMsgQueue;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;

/**
 * 对应于amqp的queue类
 *
 * @author dongyue
 * @date 2020年04月20日18:55:40
 */
public final class Queue extends FeatureBased implements Class {

    private static final int classId = 50;

    @Override
    public int classId() {
        return classId;
    }

    @Override
    public String desc() {
        return "work with queues";
    }


    /**
     * 声明队列
     */
    public class Declare extends BaseTransactionalCommand<DeclareOk> {

        private String reserved1;

        private String queue;

        private boolean passive;

        private boolean durable;

        private boolean exclusive;

        private boolean autoDelete;

        private boolean noWait;

        private Map<String, Object> arguments;


        public Declare(String reserved1, String queue, boolean passive, boolean durable, boolean exclusive, boolean autoDelete, boolean noWait, Map<String, Object> arguments) {
            super(classId, 10);
            this.reserved1 = reserved1;
            this.queue = queue;
            this.passive = passive;
            this.durable = durable;
            this.exclusive = exclusive;
            this.autoDelete = autoDelete;
            this.noWait = noWait;
            this.arguments = arguments;
        }

        @Override
        public String desc() {
            return "declare queue, create if needed";
        }

        @Override
        protected DeclareOk executeInternal() {
            VirtualHost virtualHost = (VirtualHost) getFeature(FeatureKeys.Command.VIRTUALHOST);
            Registrar<String, MessageQueue> msgQueueRegistry = virtualHost.getMsgQueueRegistry();
            if (passive) {
                //If set, the server will reply with Declare­Ok if the exchange already exists with the same name, and raise an error if not.
                MessageQueue msgQueue = msgQueueRegistry.get(queue);
                Assert.notNull(msgQueue, ExceptionMessages.buildExceptionMsgWithTemplate("current # not contains #", virtualHost.name(), queue));
                return new DeclareOk(queue, msgQueue.msgCount(), msgQueue.consumerCount());
            } else {
                //构建基础的exchange对象
                StandardMsgQueue newMsgQueue = new StandardMsgQueue(queue, durable, exclusive, autoDelete);
                newMsgQueue.updateCurrState(LifeCyclePhases.Exchange.CREATE, LifeCyclePhases.Status.START);
                //解析特定的路由规则 todo dongyue

                //注册之
                msgQueueRegistry.register(newMsgQueue.name(), newMsgQueue);
                newMsgQueue.updateCurrState(LifeCyclePhases.Exchange.CREATE, LifeCyclePhases.Status.FINISH);
                return new DeclareOk(queue, 0, 0);
            }


        }


    }

    public class DeclareOk extends BaseCommand<Void> {

        private final String queue;

        private final int msgCnt;

        private final int consumerCnt;

        public DeclareOk(String queue, int msgCnt, int consumerCnt) {
            super(classId, 11);
            this.queue = queue;
            this.msgCnt = msgCnt;
            this.consumerCnt = consumerCnt;
        }

        @Override
        public String desc() {
            return "confirms a queue definition";
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }

        public String getQueue() {
            return queue;
        }

        public int getMsgCnt() {
            return msgCnt;
        }

        public int getConsumerCnt() {
            return consumerCnt;
        }
    }

    /**
     * 队列与交换器绑定
     * If the queue name is empty, the server uses the last queue declared on the channel.
     * If the routing key is also empty, the server uses this queue name for the routing key as well.
     * If the queue name is provided but the routing key is empty, the server does the binding with that empty routing key.
     * The meaning of empty routing keys depends on the exchange implementation.
     */
    public class Bind extends BaseTransactionalCommand<BindOk> {

        private String reserved1;

        private String queue;

        private String exchange;

        private String routingKey;

        private String noWait;

        private Map<String, Object> arguments;

        public Bind(String reserved1, String queue, String exchange, String routingKey, String noWait, Map<String, Object> arguments) {
            super(classId, 20);
            this.reserved1 = reserved1;
            this.queue = queue;
            this.exchange = exchange;
            this.routingKey = routingKey;
            this.noWait = noWait;
            this.arguments = arguments;
        }

        @Override
        public String desc() {
            return "bind queue to an exchange";
        }

        @Override
        protected BindOk executeInternal() {
            VirtualHost virtualHost = (VirtualHost) getFeature(FeatureKeys.Command.VIRTUALHOST);
            Registrar<String, MessageQueue> msgQueueRegistry = virtualHost.getMsgQueueRegistry();
            Registrar<String, Exchange> exchangeRegistrar = virtualHost.getExchangeRegistry();

            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);

            if (!exchangeRegistrar.contains(exchange)) {
                throw new ChannelException(ExceptionMessages.ChannelErrors.NOT_FOUND);
            }

            Exchange targetExchange = exchangeRegistrar.get(exchange);
            MessageQueue targetQueue;
            String routingKey = this.routingKey;

            if (StringUtils.isBlank(queue)) {
                targetQueue = channel.lastDeclareMsgQueue();
                if (Objects.isNull(targetQueue)) {
                    throw new ChannelException(ExceptionMessages.ChannelErrors.NOT_FOUND);
                }
                routingKey = StringUtils.isNotBlank(routingKey) ? routingKey : targetQueue.name();
                targetQueue.bind(targetExchange, routingKey, arguments);
                return new BindOk();
            } else {
                //参数检查
                if (!msgQueueRegistry.contains(queue) || !exchangeRegistrar.contains(exchange)) {
                    throw new ChannelException(ExceptionMessages.ChannelErrors.NOT_FOUND);
                }
                targetQueue = msgQueueRegistry.get(queue);
                targetExchange = exchangeRegistrar.get(exchange);
                targetQueue.bind(targetExchange, routingKey, arguments);
                return new BindOk();
            }
        }
    }

    public class BindOk extends BaseCommand<Void> {

        public BindOk() {
            super(classId, 21);
        }

        @Override
        public String desc() {
            return "confirm bind successful";
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    /*
     * 队列与交换器解绑
     */
    public class Unbind extends BaseTransactionalCommand<UnbindOk> {

        private String reserved1;

        private String queue;

        private String exchange;

        private String routingKey;

        private Map<String, Object> arguments;

        public Unbind(String reserved1, String queue, String exchange, String routingKey, Map<String, Object> arguments) {
            super(classId, 50);
            this.reserved1 = reserved1;
            this.queue = queue;
            this.exchange = exchange;
            this.routingKey = routingKey;
            this.arguments = arguments;
        }

        @Override
        public String desc() {
            return "unbind a queue from an exchange";
        }

        @Override
        protected UnbindOk executeInternal() {
            VirtualHost virtualHost = (VirtualHost) getFeature(FeatureKeys.Command.VIRTUALHOST);
            Registrar<String, MessageQueue> msgQueueRegistry = virtualHost.getMsgQueueRegistry();
            Registrar<String, Exchange> exchangeRegistrar = virtualHost.getExchangeRegistry();

            Channel channel = (Channel) getFeature(FeatureKeys.Command.AMQP_CHANNEL);

            if (!exchangeRegistrar.contains(exchange)) {
                throw new ChannelException(ExceptionMessages.ChannelErrors.NOT_FOUND);
            }

            Exchange targetExchange = exchangeRegistrar.get(exchange);
            MessageQueue targetQueue;
            String routingKey = this.routingKey;

            if (StringUtils.isBlank(queue)) {
                targetQueue = channel.lastDeclareMsgQueue();
                if (Objects.isNull(targetQueue)) {
                    throw new ChannelException(ExceptionMessages.ChannelErrors.NOT_FOUND);
                }
                routingKey = StringUtils.isNotBlank(routingKey) ? routingKey : targetQueue.name();
                targetQueue.unBind(targetExchange, routingKey, arguments);
                return new UnbindOk();
            } else {
                //参数检查
                if (!msgQueueRegistry.contains(queue) || !exchangeRegistrar.contains(exchange)) {
                    throw new ChannelException(ExceptionMessages.ChannelErrors.NOT_FOUND);
                }
                targetQueue = msgQueueRegistry.get(queue);
                targetExchange = exchangeRegistrar.get(exchange);
                targetQueue.unBind(targetExchange, routingKey, arguments);
                return new UnbindOk();
            }
        }
    }

    public class UnbindOk extends BaseCommand<Void> {

        public UnbindOk() {
            super(classId, 51);
        }

        @Override
        public String desc() {
            return "confirm unbind successful";
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    /**
     * 清除队列中的内容
     */
    public class Purge extends BaseTransactionalCommand<PurgeOk> {

        private String reserved1;

        private String queue;

        private String noWait;

        public Purge(String reserved1, String queue, String noWait) {
            super(classId, 30);
            this.reserved1 = reserved1;
            this.queue = queue;
            this.noWait = noWait;
        }

        @Override
        public String desc() {
            return "purge a queue";
        }

        @Override
        protected PurgeOk executeInternal() {
            VirtualHost virtualHost = (VirtualHost) getFeature(FeatureKeys.Command.VIRTUALHOST);
            Registrar<String, MessageQueue> msgQueueRegistry = virtualHost.getMsgQueueRegistry();
            if (!msgQueueRegistry.contains(queue)) {
                return new PurgeOk(0);
            } else {
                MessageQueue msgQueue = msgQueueRegistry.get(queue);
                return new PurgeOk(msgQueue.purge());
            }
        }
    }

    public class PurgeOk extends BaseCommand<Void> {

        private final int msgCnt;

        public PurgeOk(int msgCnt) {
            super(classId, 31);
            this.msgCnt = msgCnt;
        }

        @Override
        public String desc() {
            return "confirms a queue purge";
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    /**
     * 删除队列
     */
    public class Delete extends BaseTransactionalCommand<DeleteOk> {

        private String reserved1;

        private String queue;

        private boolean ifUnused;

        private boolean ifEmpty;

        public Delete(String reserved1, String queue, boolean ifUnused, boolean ifEmpty) {
            super(classId, 40);
            this.reserved1 = reserved1;
            this.queue = queue;
            this.ifUnused = ifUnused;
            this.ifEmpty = ifEmpty;
        }

        @Override
        public String desc() {
            return "delete a queue";
        }

        @Override
        protected DeleteOk executeInternal() {
            VirtualHost virtualHost = (VirtualHost) getFeature(FeatureKeys.Command.VIRTUALHOST);
            Registrar<String, MessageQueue> msgQueueRegistry = virtualHost.getMsgQueueRegistry();
            if (!msgQueueRegistry.contains(queue)) {
                return new DeleteOk(0);
            } else {
                MessageQueue msgQueue = msgQueueRegistry.get(queue);
                int msgCnt = msgQueue.msgCount();
                try {
                    msgQueue.lock();
                    if (ifUnused) {
                        if (Objects.equals(LifeCyclePhases.MessageQueue.BIND, msgQueue.currPhase())) {
                            msgQueueRegistry.remove(queue);
                        }
                    } else if (ifEmpty) {
                        if (msgCnt == 0) {
                            msgQueueRegistry.remove(queue);
                        }
                    } else {
                        msgQueueRegistry.remove(queue);
                    }
                } finally {
                    msgQueue.unlock();
                }
                return new DeleteOk(msgCnt);
            }

        }
    }

    public class DeleteOk extends BaseCommand<Void> {

        private final int msgCnt;

        public DeleteOk(int msgCnt) {
            super(classId, 41);
            this.msgCnt = msgCnt;
        }

        @Override
        public String desc() {
            return "confirm deletion of a queue";
        }

        @Override
        public Void execute() {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.COMMAND_INVALID);
        }
    }

    static {
        ApplicationContext context = ApplicationContextHolder.getApplicationContext();
        MethodResolver methodResolver = context.getBean(MethodResolver.class);
        methodResolver.register(classId, 51, UnbindOk.class);
        methodResolver.register(classId, 50, Unbind.class);
        methodResolver.register(classId, 41, DeleteOk.class);
        methodResolver.register(classId, 40, Delete.class);
        methodResolver.register(classId, 31, PurgeOk.class);
        methodResolver.register(classId, 30, Purge.class);
        methodResolver.register(classId, 21, BindOk.class);
        methodResolver.register(classId, 20, Bind.class);
        methodResolver.register(classId, 11, DeclareOk.class);
        methodResolver.register(classId, 10, Declare.class);
    }
}
