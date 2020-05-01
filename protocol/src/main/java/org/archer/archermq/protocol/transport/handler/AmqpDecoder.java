package org.archer.archermq.protocol.transport.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.transport.ConnectionException;
import org.archer.archermq.protocol.transport.Frame;
import org.archer.archermq.protocol.transport.FrameBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Objects;


/***
 * 最初进入的字节解码器，用于将字节转换成帧对象.
 * 要读取一个帧，我们必须:
 * 1. 读取header,检查帧类型(frame type)和通道(channel).
 * 2. 根据帧类型，我们读取负载并进行处理.
 * 3. 读取帧结束字节.
 *
 * @date 2020年04月14日16:53:43
 * @author dongyue
 */
public class AmqpDecoder extends LengthFieldBasedFrameDecoder {

    public AmqpDecoder() {
        super(Integer.MAX_VALUE, 3, 4, 1, 0, true);
    }


    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf rawByteFrame = (ByteBuf) super.decode(ctx, in);
        if(Objects.isNull(rawByteFrame)){
            return null;
        }
        byte rawType = rawByteFrame.readByte();
        short channelId = rawByteFrame.readShort();
        int size = rawByteFrame.readInt();
        ByteBuf payload = rawByteFrame.copy(7,size);
        rawByteFrame.skipBytes(size);
        byte frameEnd = rawByteFrame.readByte();
        if(!Objects.equals(frameEnd,Frame.FRAME_END)){
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.FRAME_ERR);
        }
        return FrameBuilder.allocateFrame(rawType,channelId,payload,ctx.channel());
    }

    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void setSingleDecode(boolean singleDecode) {
        super.setSingleDecode(singleDecode);
    }

    public boolean isSingleDecode() {
        return super.isSingleDecode();
    }

    public void setCumulator(ByteToMessageDecoder.Cumulator cumulator) {
        super.setCumulator(cumulator);
    }

    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void setDiscardAfterReads(int discardAfterReads) {
        super.setDiscardAfterReads(discardAfterReads);
    }
}
