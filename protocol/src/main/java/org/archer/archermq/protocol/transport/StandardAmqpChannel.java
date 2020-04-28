package org.archer.archermq.protocol.transport;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.common.log.LogInfo;
import org.archer.archermq.common.utils.HashUtil;
import org.archer.archermq.protocol.*;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.archer.archermq.protocol.model.Command;
import org.archer.archermq.protocol.transport.bootstrap.PublishTag;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

public class StandardAmqpChannel extends BaseLifeCycleSupport implements Channel {

    private Connection amqpConn;

    private final String id;

    private final String name;

    public boolean isFlow() {
        return flow;
    }

    private boolean flow;

    private int prefetchSize;

    private short prefetchCount;

    private PublishTag publishTag;

    private Set<MessageQueue> consumingMessageQueue = Sets.newConcurrentHashSet();

    private Stack<Command<?>> invokeStack = new Stack<>();

    private Queue<Frame> rawFrameQueue = new ConcurrentLinkedQueue<>();

    private AtomicReference<Map<String, Message>> unConfirmedMsgRef = new AtomicReference<>();

    private MessageQueue theLastDeclareMessageQueue;

    public StandardAmqpChannel(String id) {
        this(id, id, null);
    }

    public StandardAmqpChannel(String id, String name, Connection amqpConn) {
        updateCurrState(LifeCyclePhases.Channel.CREATE, LifeCyclePhases.Status.START);
        triggerEvent();
        this.id = StringUtils.isBlank(id) ? HashUtil.hash() : id;
        this.name = name;
        this.amqpConn = amqpConn;
        if (Objects.nonNull(amqpConn)) {
            this.prefetchSize = amqpConn.getPreFetchSize();
            this.prefetchCount = amqpConn.getPreFetchCount();
        }
        updateCurrState(LifeCyclePhases.Channel.CREATE, LifeCyclePhases.Status.START);
        triggerEvent();

    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Connection conn() {
        return amqpConn;
    }

    @Override
    public void setFlow(boolean active) {
        this.flow = active;
    }

    @Override
    public void close() {
        updateCurrState(LifeCyclePhases.Channel.CLOSE, LifeCyclePhases.Status.START);
        triggerEvent();

        this.amqpConn.remove(id);

        updateCurrState(LifeCyclePhases.Channel.CLOSE, LifeCyclePhases.Status.START);
        triggerEvent();

    }

    @Override
    public Set<MessageQueue> consuming() {
        return Sets.newHashSet(consumingMessageQueue);
    }

    @Override
    public MessageQueue lastDeclareMsgQueue() {
        return theLastDeclareMessageQueue;
    }

    @Override
    public void qos(int prefetchSize, short prefetchCount) {
        LogInfo logInfo = BizLogUtil.start()
                .setLayer(LogConstants.TRANSPORT_LAYER)
                .setType(LogConstants.CONFIG_CHANGED)
                .addContent(LogConstants.CONFIG_ITEM_NAME, "qos")
                .addContent(LogConstants.CONFIG_ITEM_BEFORE, "qos{prefetchSize:" + this.prefetchSize + ",prefetchCount:" + this.prefetchCount + "}");

        this.prefetchSize = prefetchSize;
        this.prefetchCount = prefetchCount;

        logInfo.addContent(LogConstants.CONFIG_ITEM_AFTER, "qos{prefetchSize" + this.prefetchSize + ",prefetchCount:" + this.prefetchCount + "}");
        BizLogUtil.record(logInfo);
    }

    @Override
    public Stack<Command<?>> invokeStack() {
        return invokeStack;
    }

    @Override
    public Frame poll() {
        return this.rawFrameQueue.poll();
    }

    @Override
    public void offer(Frame frame) {
        this.rawFrameQueue.offer(frame);
    }

    @Override
    public Frame peak() {
        return this.rawFrameQueue.peek();
    }

    @Override
    public List<Message> unConfirmedMsg() {
        return Lists.newArrayList(unConfirmedMsgRef.get().values());
    }

    @Override
    public void confirmMsg(String deliveryTag, boolean multiple) {
        Message needConfirmMsg = unConfirmedMsgRef.get().remove(deliveryTag);
        if (Objects.nonNull(needConfirmMsg)) {
            needConfirmMsg.updateCurrState(LifeCyclePhases.Message.CONFIRMED, LifeCyclePhases.Status.FINISH);
            needConfirmMsg.triggerEvent();
        }
    }

    @Override
    public void confirmAllMsg() {
        Map<String, Message> unConfirmedMsg = unConfirmedMsgRef.getAndSet(new LinkedHashMap<>());
        if (!CollectionUtils.isEmpty(unConfirmedMsg)) {
            unConfirmedMsg.forEach((msgKey, msg) -> {
                msg.updateCurrState(LifeCyclePhases.Message.CONFIRMED, LifeCyclePhases.Status.FINISH);
                msg.triggerEvent();
            });
        }
    }

    @Override
    public void exchange(Message msg) {
        VirtualHost virtualHost = getAmqpConn().virtualHost();
        Map<String, Object> msgProperties = msg.msgProperties();
        String exchangeName = Optional.ofNullable((String) msgProperties.get(FeatureKeys.Message.EXCHANGE_NAME)).orElse(publishTag.getExchange());
        String routingKey = Optional.ofNullable((String) msgProperties.get(FeatureKeys.Message.ROUTING_KEY)).orElse(publishTag.getRoutingKey());
        //ignore
        boolean mandatory = Optional.ofNullable((Boolean) msgProperties.get(FeatureKeys.Message.MANDATORY)).orElse(publishTag.isMandatory());
        boolean immediate = Optional.ofNullable((Boolean) msgProperties.get(FeatureKeys.Message.IMMEDIATE)).orElse(publishTag.isMandatory());
        Exchange exchange = virtualHost.getExchangeRegistry().get(exchangeName);
        Assert.notNull(exchange, ExceptionMessages.buildExceptionMsgWithTemplate("cannot find the # exchange", exchangeName));
        if (immediate) {
            exchange.exchangeMsg(msg, routingKey);
        } else {
            ExecutorService taskPool = virtualHost.taskPool();
            taskPool.submit(() -> exchange.exchangeMsg(msg, routingKey));
        }

    }

    @Override
    public void redeliver(Message msg) {
        VirtualHost virtualHost = getAmqpConn().virtualHost();
        Map<String, Object> msgProperties = msg.msgProperties();
        boolean republish = (boolean) msgProperties.getOrDefault(FeatureKeys.Message.REPUBLISH, false);
        if (!republish) {
            throw new ChannelException(ExceptionMessages.ConnectionErrors.NOT_ALLOWED);
        } else {
            MessageQueue targetMsgQueue = virtualHost.getMsgQueueRegistry().get((String) msgProperties.get(FeatureKeys.Message.MESSAGE_QUEUE));
            if (Objects.isNull(targetMsgQueue)) {
                throw new ChannelException(ExceptionMessages.ChannelErrors.REDELIVER_FAILED);
            } else {
                targetMsgQueue.enqueue(msg);
            }
        }
    }

    @Override
    public void beginTx() {
        throw new ChannelException(ExceptionMessages.ConnectionErrors.NOT_IMPLEMENTED);
    }

    @Override
    public void commit() {
        throw new ChannelException(ExceptionMessages.ConnectionErrors.NOT_IMPLEMENTED);
    }

    @Override
    public void rollback() {
        throw new ChannelException(ExceptionMessages.ConnectionErrors.NOT_IMPLEMENTED);
    }

    @Override
    public void setPublishTag(PublishTag publishTag) {
        this.publishTag = publishTag;
    }

    public Connection getAmqpConn() {
        return amqpConn;
    }

    public void setAmqpConn(Connection amqpConn) {
        this.amqpConn = amqpConn;
    }

    @Override
    protected boolean couldChangeState(String nextPhase, String nextPhaseStatus) {
        return super.couldChangeState(nextPhase, nextPhaseStatus);
    }

}
