package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.BaseLifeCycleSupport;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.MessageQueue;
import org.archer.archermq.protocol.constants.StateEnum;
import org.archer.archermq.protocol.model.Command;

import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class StandardAmqpChannel extends BaseLifeCycleSupport implements Channel {




    @Override
    public String id() {
        return null;
    }

    @Override
    public String name() {
        return null;
    }

    @Override
    public void setFlow(boolean active) {

    }

    @Override
    public void close() {

    }

    @Override
    public Set<MessageQueue> consuming() {
        return null;
    }

    @Override
    public MessageQueue lastDeclareMsgQueue() {
        return null;
    }

    @Override
    public void qos(int prefetchSize, short prefetchCount) {

    }

    @Override
    public Stack<Command<?>> invokeStack() {
        return null;
    }

    @Override
    public Frame pop() {
        return null;
    }

    @Override
    public void push(Frame frame) {

    }

    @Override
    public Frame peak() {
        return null;
    }

    @Override
    public Message selectMsg() {
        return null;
    }

    @Override
    public Queue<Message> unConfirmedMsg() {
        return null;
    }

    @Override
    public void confirmMsg(String deliveryTag, boolean multiple) {

    }

    @Override
    public void confirmAllMsg() {

    }

    @Override
    public void exchange(Message msg) {

    }

    @Override
    public void redeliver(Message msg) {

    }

    @Override
    public void beginTx() {

    }

    @Override
    public void commit() {

    }

    @Override
    public void rollback() {

    }
}
