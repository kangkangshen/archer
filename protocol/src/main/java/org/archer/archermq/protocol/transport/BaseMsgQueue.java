package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.*;
import org.archer.archermq.protocol.constants.DeliverMode;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class BaseMsgQueue extends BaseLifeCycleSupport implements MessageQueue {

    private final Queue<Message> memQueue = new ConcurrentLinkedDeque<>();

    private final String name;

    private final boolean durable;

    private final boolean exclusive;

    private final boolean autoDelete;

    private String bindingKey;

    private Exchange exchange;

    public BaseMsgQueue(String name, boolean durable, boolean exclusive, boolean autoDelete) {
        this.name = name;
        this.durable = durable;
        this.exclusive = exclusive;
        this.autoDelete = autoDelete;
    }

    @Override
    public void setDeliverMode(DeliverMode deliverMode) {

    }

    @Override
    public String bindingKey() {
        return bindingKey;
    }

    @Override
    public void bind(Exchange exchange, String routingKey, Map<String, Object> args) {

    }

    @Override
    public void unBind(Exchange exchange, String routingKey, Map<String, Object> args) {

    }

    @Override
    public int purge() {
        return 0;
    }

    @Override
    public void lock() {

    }

    @Override
    public void unlock() {

    }

    @Override
    public void dequeue(Message message) {

    }

    @Override
    public Message enqueue() {
        return null;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int msgCount() {
        return 0;
    }

    @Override
    public int localMsgCnt() {
        return 0;
    }

    @Override
    public int consumerCount() {
        return 0;
    }

    @Override
    public Exchange exchange() {
        return null;
    }

    @Override
    public Registrar<String, Consumer> getConsumerRegistry() {
        return null;
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


}
