package org.archer.archermq.protocol.transport.bootstrap;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.bootstrap.ServerBootstrapConfig;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.protocol.constants.ServerRoleTypeEnum;
import org.archer.archermq.protocol.transport.handler.AmqpDecoder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 基于netty实现的amqpServerContainer
 *
 * @author dongyue
 * @date 2020年04月15日17:48:30
 */
@Component
public class AmqpServerContainer implements InitializingBean {

    @Value("amqp.server.port")
    private int amqpServerPort = 5672;

    @Value("server.listen.threads")
    private int serverListenThreads = 8;

    @Value("server.handle.threads")
    private int serverHandleThreads = 8;

    @Value("server.role.type")
    private int serverRoleType = 1;
    private ServerRoleTypeEnum serverRoleTypeEnum;

    private

    @Autowired
    private AmqpDecoder amqpDecoder;

    private ServerBootstrap serverBootstrap;

    private ServerBootstrapConfig serverBootstrapConfig;

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(serverListenThreads);
        EventLoopGroup workerGroup = new NioEventLoopGroup(serverHandleThreads);
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrapConfig = serverBootstrap.config();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(amqpDecoder);
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = serverBootstrap.bind(amqpServerPort).sync();
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public ServerBootstrap getServerBootstrap() {
        return serverBootstrap;
    }

    public ServerBootstrapConfig getServerBootstrapConfig() {
        return serverBootstrapConfig;
    }
}
