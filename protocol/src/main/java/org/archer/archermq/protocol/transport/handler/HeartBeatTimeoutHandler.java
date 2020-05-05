package org.archer.archermq.protocol.transport.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;

public class HeartBeatTimeoutHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            BizLogUtil.start()
                    .setLayer(LogConstants.TRANSPORT_LAYER)
                    .setType(LogConstants.HEARTBEAT)
                    .appendContent(event.state().name());
        }else{
            //其他事件不关心
            ctx.fireUserEventTriggered(evt);
        }
    }
}
