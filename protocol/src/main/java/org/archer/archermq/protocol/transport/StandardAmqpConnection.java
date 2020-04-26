package org.archer.archermq.protocol.transport;

import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.common.log.LogInfo;
import org.archer.archermq.common.utils.HashUtil;
import org.archer.archermq.protocol.*;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Set;

public class StandardAmqpConnection extends BaseLifeCycleSupport implements Connection {

    private static final Logger logger = LoggerFactory.getLogger("");

    private final VirtualHost virtualHost;

    private final Registrar<String, Channel> channelRegistrar;

    private int prefetchSize;

    private short prefetchCount;

    private final String id;

    public StandardAmqpConnection(VirtualHost virtualHost) {
        this(HashUtil.hash(),virtualHost);
    }

    public StandardAmqpConnection(String id, VirtualHost virtualHost) {
        this.channelRegistrar = new StandardMemRegistrar<>();
        this.id = id;
        this.virtualHost = virtualHost;
        updateCurrState(LifeCyclePhases.Connection.CREATE, LifeCyclePhases.Status.START);
        triggerEvent();
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public Channel openChannel() {
        //todo 这里需要打日志
        LogInfo logInfo = BizLogUtil.start().setLayer(LogConstants.TRANSPORT_LAYER).setType(LogConstants.INSTANCE_CREATED);

        StandardAmqpChannel channel = new StandardAmqpChannel(HashUtil.hash());
        channelRegistrar.register(channel.id(), channel);
        channel.setAmqpConn(this);

        logInfo.addContent(LogConstants.INSTANCE, channel.id());
        BizLogUtil.record(logInfo);
        return channel;
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
    public int getPreFetchSize() {
        return prefetchSize;
    }

    @Override
    public short getPreFetchCount() {
        return prefetchCount;
    }

    @Override
    public VirtualHost virtualHost() {
        return virtualHost;
    }


    @Override
    public boolean contains(String s) {
        return channelRegistrar.contains(s);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public boolean register(String s, Channel instance) {
        return channelRegistrar.register(s, instance);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public Channel remove(String s) {
        return channelRegistrar.remove(s);
    }

    @Override
    public Channel get(String s) {
        return channelRegistrar.get(s);
    }

    @Override
    public Set<String> ids() {
        return channelRegistrar.ids();
    }

    @Override
    public List<Channel> instances() {
        return channelRegistrar.instances();
    }

    @Override
    public int size() {
        return channelRegistrar.size();
    }

}
