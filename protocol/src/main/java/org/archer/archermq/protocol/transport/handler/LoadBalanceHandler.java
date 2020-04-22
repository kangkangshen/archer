package org.archer.archermq.protocol.transport.handler;

import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

/**
 * 服务器端负载均衡
 *
 * @author dongyue
 * @date 2020年04月16日23:25:36
 */
@Component
public class LoadBalanceHandler extends ChannelInboundHandlerAdapter {

}
