package org.archer.archermq.protocol.transport;

import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.common.utils.HashUtil;
import org.archer.archermq.protocol.BaseLifeCycleSupport;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.MessageQueue;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.archer.archermq.protocol.constants.StateEnum;
import org.archer.archermq.protocol.model.Command;
import org.springframework.util.Assert;

import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class StandardAmqpChannel extends BaseLifeCycleSupport implements Channel {


    private String id;

    private String name;

    private boolean flow;

    public StandardAmqpChannel(String id) {
        this.id = StringUtils.isBlank(id)? HashUtil.hash():id;
        this.name = id;
        updateCurrState(LifeCyclePhases.Channel.CREATE, LifeCyclePhases.Status.START);
    }

    public StandardAmqpChannel(String id, String name) {
        this.id = id;
        this.name = name;
    }

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
        this.flow = active;
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
