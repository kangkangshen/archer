package org.archer.archermq.protocol.transport;

import org.archer.archermq.common.utils.HashUtil;
import org.archer.archermq.protocol.BaseLifeCycleSupport;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.Connection;
import org.archer.archermq.protocol.Registrar;
import org.archer.archermq.protocol.constants.LifeCyclePhases;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class StandardAmqpConnection extends BaseLifeCycleSupport implements Connection, Registrar<String,Channel> {

    private final Registrar<String,Channel> channelRegistrar;

    public StandardAmqpConnection() {
        this.channelRegistrar = new StandardMemRegistrar<>();
        updateCurrState(LifeCyclePhases.Connection.CREATE,LifeCyclePhases.Status.START);
        triggerEvent();
    }

    @Override
    public Channel openChannel() {
        Random random = new Random();
        Channel channel = new StandardAmqpChannel(HashUtil.md5(random.nextLong()));

    }

    @Override
    public void qos(int prefetchSize, short prefetchCount) {

    }

    @Override
    public boolean contains(String s) {
        return false;
    }

    @Override
    public boolean register(String s, Channel instance) {
        return false;
    }

    @Override
    public Channel remove(String s) {
        return null;
    }

    @Override
    public Channel get(String s) {
        return null;
    }

    @Override
    public Set<String> ids() {
        return null;
    }

    @Override
    public List<Channel> instances() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

}
