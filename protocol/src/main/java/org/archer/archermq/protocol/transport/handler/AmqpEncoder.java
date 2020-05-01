package org.archer.archermq.protocol.transport.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.archer.archermq.protocol.transport.Frame;

public class AmqpEncoder extends MessageToByteEncoder<Frame> {



    @Override
    protected void encode(ChannelHandlerContext ctx, Frame msg, ByteBuf out) throws Exception {
        out.writeByte(msg.type().getVal());
        out.writeShort(msg.channelId());
        out.writeInt(msg.size());
        out.writeBytes(msg.content());
        out.writeByte(msg.frameEnd());
    }
}
