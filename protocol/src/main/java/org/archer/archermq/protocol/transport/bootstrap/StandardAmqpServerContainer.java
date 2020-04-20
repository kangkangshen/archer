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
import org.archer.archermq.protocol.BaseLifeCycleSupport;
import org.archer.archermq.protocol.Server;
import org.archer.archermq.protocol.constants.LifeCyclePhases;
import org.archer.archermq.protocol.constants.ServerRoleTypeEnum;
import org.archer.archermq.protocol.transport.handler.AmqpDecoder;
import org.archer.archermq.protocol.transport.handler.LoadBalanceHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 基于netty实现的amqpServerContainer
 *
 * @author dongyue
 * @date 2020年04月15日17:48:30
 */
@Component
public class StandardAmqpServerContainer extends BaseLifeCycleSupport implements InitializingBean,Server {

    @Value("amqp.server.port")
    private int amqpServerPort = 5672;

    @Value("server.listen.threads")
    private int serverListenThreads = 8;

    @Value("server.handle.threads")
    private int serverHandleThreads = 8;

    @Value("server.role.type")
    private int serverRoleType;
    private ServerRoleTypeEnum serverRoleTypeEnum;


    @Autowired
    private AmqpDecoder amqpDecoder;

    @Autowired
    private LoadBalanceHandler loadBalanceHandler;

    private ServerBootstrap serverBootstrap;

    private ServerBootstrapConfig serverBootstrapConfig;

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void afterPropertiesSet() {
        serverRoleTypeEnum = ServerRoleTypeEnum.getByVal(serverRoleType);
        Assert.notNull(serverRoleTypeEnum,"wrong server role type");
        updateCurrState(LifeCyclePhases.Server.STARTING, LifeCyclePhases.Status.START);
        triggerEvent();
        start();
        updateCurrState(LifeCyclePhases.Server.RUNNING,LifeCyclePhases.Status.START);
        triggerEvent();
    }

    public ServerBootstrap getServerBootstrap() {
        return serverBootstrap;
    }

    public ServerBootstrapConfig getServerBootstrapConfig() {
        return serverBootstrapConfig;
    }

    @Override
    public void start() {

        EventLoopGroup bossGroup = new NioEventLoopGroup(serverListenThreads);
        EventLoopGroup workerGroup = new NioEventLoopGroup(serverHandleThreads);
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(amqpDecoder);
                            if(ServerRoleTypeEnum.PIONEER.equals(serverRoleTypeEnum)){
                                //作为将军，肯定要多感谢事情啦，就比如派活啦 blablabla。。。
                                ch.pipeline().addLast(loadBalanceHandler);
                            }
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrapConfig = serverBootstrap.config();
            ChannelFuture f = serverBootstrap.bind(amqpServerPort).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


    public ServerRoleTypeEnum getServerRoleTypeEnum() {
        return serverRoleTypeEnum;
    }
}
