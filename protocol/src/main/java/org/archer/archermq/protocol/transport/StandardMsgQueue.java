package org.archer.archermq.protocol.transport;

import org.archer.archermq.common.register.Registrar;
import org.archer.archermq.common.register.StandardMemRegistrar;
import org.archer.archermq.protocol.*;
import org.archer.archermq.protocol.constants.DeliverMode;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.ReentrantLock;

public class StandardMsgQueue extends BaseLifeCycleSupport implements MessageQueue {

    private final Queue<Message> memQueue = new ConcurrentLinkedDeque<>();

    private final ReentrantLock locker = new ReentrantLock(true);

    private final String name;

    private final boolean durable;

    private final boolean exclusive;

    private final boolean autoDelete;

    private DeliverMode deliverMode = DeliverMode.PULL;

    private String bindingKey;

    private Exchange exchange;

    private final Registrar<String, Consumer> consumerRegistry = new StandardMemRegistrar<>();

    public StandardMsgQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
        this.name = name;
        this.durable = durable;
        this.exclusive = exclusive;
        this.autoDelete = autoDelete;
    }

    @Override
    public void setDeliverMode(DeliverMode deliverMode) {
        this.deliverMode = deliverMode;
    }

    public DeliverMode getDeliverMode() {
        return deliverMode;
    }


    @Override
    public String bindingKey() {
        return bindingKey;
    }

    @Override
    public void bind(Exchange exchange, String routingKey, Map<String, Object> args) {
        updateCurrState(LifeCyclePhases.MessageQueue.BIND, LifeCyclePhases.Status.START);
        triggerEvent();
        try {
            Assert.notNull(exchange, "target exchange  is null.");
            this.exchange = exchange;
            this.bindingKey = routingKey;
            exchange.acceptMsgQueueBinding(this, routingKey);
            updateCurrState(LifeCyclePhases.MessageQueue.BIND, LifeCyclePhases.Status.FINISH);
            triggerEvent();
        } catch (Throwable throwable) {
            updateCurrState(LifeCyclePhases.MessageQueue.BIND, LifeCyclePhases.Status.ABNORMAL);
            triggerEvent();
            exchange.discardMsgQueueBinding(this, routingKey);
            throwable.printStackTrace();
        }
    }

    @Override
    public void unBind(Exchange exchange, String routingKey, Map<String, Object> args) {
        updateCurrState(LifeCyclePhases.MessageQueue.UNBIND, LifeCyclePhases.Status.START);
        triggerEvent();
        try {
            Assert.notNull(exchange, "target exchange  is null.");
            this.exchange = null;
            this.bindingKey = null;
            exchange.discardMsgQueueBinding(this, routingKey);
            updateCurrState(LifeCyclePhases.MessageQueue.UNBIND, LifeCyclePhases.Status.FINISH);
            triggerEvent();
        } catch (Throwable throwable) {
            updateCurrState(LifeCyclePhases.MessageQueue.UNBIND, LifeCyclePhases.Status.ABNORMAL);
            triggerEvent();
            //重试一次
            exchange.discardMsgQueueBinding(this, routingKey);
            throwable.printStackTrace();
        }
    }

    @Override
    public int purge() {
        locker.lock();
        int msgCnt = 0;
        try {
            msgCnt = memQueue.size();
            memQueue.clear();
        } finally {
            locker.unlock();
        }
        return msgCnt;
    }

    @Override
    public void lock() {
        locker.lock();
    }

    @Override
    public void unlock() {
        locker.unlock();
    }

    @Override
    public void enqueue(Message message) {
        message.updateCurrState(LifeCyclePhases.Message.ENQUEUE, LifeCyclePhases.Status.START);
        message.triggerEvent();
        try {
            memQueue.offer(message);
            message.updateCurrState(LifeCyclePhases.Message.ENQUEUE, LifeCyclePhases.Status.FINISH);
            message.triggerEvent();
        } catch (Exception e) {
            message.updateCurrState(LifeCyclePhases.Message.ENQUEUE, LifeCyclePhases.Status.START);
            message.triggerEvent();
        }
    }

    @Override
    public Message dequeue() {
        Message message = null;
        locker.lock();
        try {
            message = memQueue.peek();
            if (Objects.isNull(message)) {
                return null;
            } else {
                message.updateCurrState(LifeCyclePhases.Message.DEQUEUE, LifeCyclePhases.Status.START);
                message.triggerEvent();
                memQueue.poll();
            }
            message.updateCurrState(LifeCyclePhases.Message.DEQUEUE, LifeCyclePhases.Status.FINISH);
            message.triggerEvent();
            return message;
        } catch (Exception e) {
            if (Objects.nonNull(message)) {
                message.updateCurrState(LifeCyclePhases.Message.DEQUEUE, LifeCyclePhases.Status.ABNORMAL);
                message.triggerEvent();
                e.printStackTrace();
                throw e;
            }
            return null;
        } finally {
            locker.unlock();
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int msgCount() {
        //todo dongyue
        throw new UnsupportedOperationException();
    }

    @Override
    public int localMsgCnt() {
        return memQueue.size();
    }

    @Override
    public int consumerCount() {
        return consumerRegistry.ids().size();
    }

    @Override
    public Exchange exchange() {
        return exchange;
    }

    @Override
    public Registrar<String, Consumer> getConsumerRegistry() {
        return consumerRegistry;
    }

    @Override
    public boolean durable() {
        return durable;
    }

    @Override
    public boolean autoDelete() {
        return autoDelete;
    }

    @Override
    public boolean exclusive() {
        return exclusive;
    }

    @Override
    protected boolean couldChangeState(String nextPhase, String nextPhaseStatus) {
        return super.couldChangeState(nextPhase, nextPhaseStatus);
    }
}
