package org.archer.archermq.protocol.transport.bootstrap;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.bootstrap.ServerBootstrapConfig;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.config.register.Registrar;
import org.archer.archermq.config.register.StandardMemRegistrar;
import org.archer.archermq.protocol.BaseLifeCycleSupport;
import org.archer.archermq.protocol.Server;
import org.archer.archermq.protocol.VirtualHost;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.archer.archermq.protocol.constants.ServerRoleTypeEnum;
import org.archer.archermq.protocol.transport.FrameDispatcher;
import org.archer.archermq.protocol.transport.handler.AmqpDecoder;
import org.archer.archermq.protocol.transport.handler.AmqpEncoder;
import org.archer.archermq.protocol.transport.handler.HeartBeatTimeoutHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Set;

/**
 * 基于netty实现的amqpServerContainer
 *
 * @author dongyue
 * @date 2020年04月15日17:48:30
 */
@Component
public class StandardAmqpServerContainer extends BaseLifeCycleSupport implements InitializingBean, Server {

    @Value("${amqp.server.port:5672}")
    private int amqpServerPort;

    @Value("${server.listen.threads:8}")
    private int serverListenThreads;

    @Value("${server.handle.threads:8}")
    private int serverHandleThreads;

    @Value(("${server.read.idle:10}"))
    private int serverIdleTime;


    @Value("${server.role.type}")
    private int serverRoleType;
    private ServerRoleTypeEnum serverRoleTypeEnum;


    @Autowired
    private ChannelInboundHandler loadBalanceHandler;

    @Autowired
    private FrameDispatcher frameDispatcher;

    private ServerBootstrap serverBootstrap;

    private ServerBootstrapConfig serverBootstrapConfig;

    private Channel currChannel;

    private final Registrar<String, VirtualHost> virtualHostRegistry = new StandardMemRegistrar<>();

    @Override
    public void afterPropertiesSet() {
        serverRoleTypeEnum = ServerRoleTypeEnum.getByVal(serverRoleType);
        Assert.notNull(serverRoleTypeEnum, "wrong server role type");

        start();

    }

    public ServerBootstrap getServerBootstrap() {
        return serverBootstrap;
    }

    public ServerBootstrapConfig getServerBootstrapConfig() {
        return serverBootstrapConfig;
    }

    @Override
    public void start() {

        updateCurrState(LifeCyclePhases.Server.STARTING, LifeCyclePhases.Status.START);
        triggerEvent();

        BizLogUtil.start().setLayer(LogConstants.TRANSPORT_LAYER);
        BizLogUtil.recordInstanceCreated(this);

        try {
            EventLoopGroup bossGroup = new NioEventLoopGroup(serverListenThreads);
            EventLoopGroup workerGroup = new NioEventLoopGroup(serverHandleThreads);
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("amqpDecoder", new AmqpDecoder());
                            ch.pipeline().addLast("amqpEncoder", new AmqpEncoder());

                            if (ServerRoleTypeEnum.PIONEER.equals(serverRoleTypeEnum)) {
                                //作为将军，肯定要多感谢事情啦，就比如派活啦 blablabla。。。
                                ch.pipeline().addLast("loadBalanceHandler", loadBalanceHandler);
                            }
                            //amqp 协议仅支持单向心跳
                            ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(serverIdleTime, serverIdleTime, serverIdleTime));
                            ch.pipeline().addLast("heartbeatTimeoutHandler", new HeartBeatTimeoutHandler());


                            ch.pipeline().addLast("frameDispatcher", frameDispatcher);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrapConfig = serverBootstrap.config();
            ChannelFuture bindFuture = serverBootstrap.bind(amqpServerPort).sync();
            this.currChannel = bindFuture.channel();

            updateCurrState(LifeCyclePhases.Server.RUNNING, LifeCyclePhases.Status.START);
            triggerEvent();

        } catch (InterruptedException e) {

            BizLogUtil.recordException(e);

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

            serverBootstrapConfig.group().shutdownGracefully();
            serverBootstrapConfig.childGroup().shutdownGracefully();

            updateCurrState(LifeCyclePhases.Server.TERMINATE, LifeCyclePhases.Status.FINISH);
            triggerEvent();


        } catch (Exception e) {

            BizLogUtil.recordException(e);

            serverBootstrapConfig.group().shutdownGracefully();
            serverBootstrapConfig.childGroup().shutdownGracefully();

            updateCurrState(LifeCyclePhases.Server.TERMINATE, LifeCyclePhases.Status.ABNORMAL);
            triggerEvent();

            e.printStackTrace();

            //rethrow it
            throw new RuntimeException(e);

        }

    }


    public ServerRoleTypeEnum getServerRoleTypeEnum() {
        return serverRoleTypeEnum;
    }

    @Override
    public String toString() {
        return "StandardAmqpServerContainer{" +
                "amqpServerPort=" + amqpServerPort +
                ", serverListenThreads=" + serverListenThreads +
                ", serverHandleThreads=" + serverHandleThreads +
                ", serverRoleType=" + serverRoleType +
                '}';
    }

    @Override
    public boolean contains(String s) {
        return virtualHostRegistry.contains(s);
    }

    @Override
    public boolean register(String s, VirtualHost instance) {
        return virtualHostRegistry.register(s, instance);
    }

    @Override
    public VirtualHost remove(String s) {
        return virtualHostRegistry.remove(s);
    }

    @Override
    public VirtualHost get(String s) {
        return virtualHostRegistry.get(s);
    }

    @Override
    public Set<String> ids() {
        return virtualHostRegistry.ids();
    }

    @Override
    public List<VirtualHost> instances() {
        return virtualHostRegistry.instances();
    }

    @Override
    public int size() {
        return virtualHostRegistry.size();
    }
}
