package org.archer.archermq.protocol.transport;

import io.netty.channel.Channel;
import org.archer.archermq.protocol.Connection;
import org.archer.archermq.protocol.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseFrameHandler implements FrameHandler {

    @Autowired
    private Server server;

    @Override
    public Frame handleFrame(Frame frame) {
        Channel tcpChannel = frame.tcpChannel();
        server.instances().forEach(virtualHost -> {
            if (virtualHost.getConnRegistry().contains(tcpChannel)) {
                Connection amqpConnection = virtualHost.getConnRegistry().get(tcpChannel);
                StandardFrame standardFrame = (StandardFrame) frame;
                standardFrame.setConnection(amqpConnection);
                standardFrame.setChannel(amqpConnection.get(frame.channelId()));
            }
        });
        //channel建立期间为null
//        if(Objects.isNull(frame.channel())){
//            throw new ConnectionException(ExceptionMessages.ConnectionErrors.CHANNEL_ERR);
//        }
        return handleFrameInternal(frame);
    }

    protected Frame handleFrameInternal(Frame frame) {
        return null;
    }
}
