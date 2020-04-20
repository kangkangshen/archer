package org.archer.archermq.protocol.transport.bootstrap;


import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.BootstrapConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.protocol.BaseLifeCycleSupport;
import org.archer.archermq.protocol.InternalClient;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.archer.archermq.protocol.transport.handler.AmqpDecoder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SimpleAmqpClientContainer extends BaseLifeCycleSupport implements InitializingBean, InternalClient {

    @Value("amqp.internal.client.port")
    private int amqpServerPort = 5673;

    @Value("internal.client.handle.threads")
    private int serverHandleThreads = 2;

    @Autowired
    private AmqpDecoder amqpDecoder;

    private Bootstrap internalClientBootstrap;

    private BootstrapConfig internalClientBootstrapConfig;

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void afterPropertiesSet() {
        updateCurrState(LifeCyclePhases.Server.STARTING, LifeCyclePhases.Status.START);
        triggerEvent();
        start();
        updateCurrState(LifeCyclePhases.Server.RUNNING,LifeCyclePhases.Status.START);
        triggerEvent();
    }

    public Bootstrap getInternalClientBootstrap() {
        return internalClientBootstrap;
    }

    public BootstrapConfig getInternalClientBootstrapConfig() {
        return internalClientBootstrapConfig;
    }

    @Override
    public void start() {

        EventLoopGroup workerGroup = new NioEventLoopGroup(serverHandleThreads);
        try {
            internalClientBootstrap = new Bootstrap();
            internalClientBootstrap.group(workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(amqpDecoder);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.SO_KEEPALIVE, true);
            internalClientBootstrapConfig = internalClientBootstrap.config();
            ChannelFuture f = internalClientBootstrap.bind(amqpServerPort).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            workerGroup.shutdownGracefully();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
