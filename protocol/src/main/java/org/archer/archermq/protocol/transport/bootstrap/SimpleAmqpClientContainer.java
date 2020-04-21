package org.archer.archermq.protocol.transport.bootstrap;


import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.BootstrapConfig;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.SneakyThrows;
import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.protocol.BaseLifeCycleSupport;
import org.archer.archermq.protocol.InternalClient;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class SimpleAmqpClientContainer extends BaseLifeCycleSupport implements InitializingBean, InternalClient {

    @Value("${amqp.internal.client.port:5673}")
    private int amqpInternalClientPort;

    @Value("${amqp.internal.client.threads:2}")
    private int amqpInternalClientThreads;

    @Autowired
    private ChannelInboundHandler amqpDecoder;

    private Bootstrap internalClientBootstrap;

    private BootstrapConfig internalClientBootstrapConfig;

    private Channel currChannel;

    @Override
    public void afterPropertiesSet() {

        start();

    }

    public Bootstrap getInternalClientBootstrap() {
        return internalClientBootstrap;
    }

    public BootstrapConfig getInternalClientBootstrapConfig() {
        return internalClientBootstrapConfig;
    }

    @Override
    public void start() {

        updateCurrState(LifeCyclePhases.Server.STARTING, LifeCyclePhases.Status.START);
        triggerEvent();

        BizLogUtil.start().setLayer(LogConstants.TRANSPORT_LAYER);
        BizLogUtil.recordInstanceCreated(this);

        try {
            EventLoopGroup workerGroup = new NioEventLoopGroup(amqpInternalClientThreads);
            internalClientBootstrap = new Bootstrap();
            internalClientBootstrap.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(amqpDecoder);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            internalClientBootstrapConfig = internalClientBootstrap.config();
            ChannelFuture bindFeature = internalClientBootstrap.bind(amqpInternalClientPort).sync();
            this.currChannel = bindFeature.channel();

            updateCurrState(LifeCyclePhases.Server.RUNNING, LifeCyclePhases.Status.START);
            triggerEvent();

        } catch (Exception e) {

            BizLogUtil.recordException(e);
            BizLogUtil.end();

            updateCurrState(LifeCyclePhases.Server.STARTING, LifeCyclePhases.Status.ABNORMAL);
            triggerEvent();

            destroy();

        }
    }

    @Override
    @PreDestroy
    public void destroy() {

        updateCurrState(LifeCyclePhases.Server.TERMINATE, LifeCyclePhases.Status.START);
        triggerEvent();

        BizLogUtil.start().setLayer(LogConstants.TRANSPORT_LAYER);
        BizLogUtil.recordInstanceDestroyed(this);

        try {

            this.currChannel.close().sync();
            this.internalClientBootstrapConfig.group().shutdownGracefully();

            updateCurrState(LifeCyclePhases.Server.TERMINATE, LifeCyclePhases.Status.FINISH);
            triggerEvent();

        } catch (Exception e) {

            BizLogUtil.recordException(e);
            BizLogUtil.end();

            this.internalClientBootstrapConfig.group().shutdownGracefully();

            updateCurrState(LifeCyclePhases.Server.TERMINATE, LifeCyclePhases.Status.ABNORMAL);
            triggerEvent();

            e.printStackTrace();

            //rethrow it
            throw new RuntimeException(e);

        }
    }

    @Override
    public String toString() {
        return "SimpleAmqpClientContainer{" +
                "amqpInternalClientPort=" + amqpInternalClientPort +
                ", amqpInternalClientThreads=" + amqpInternalClientThreads +
                '}';
    }
}
