package org.archer.archermq.protocol.transport.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.archer.archermq.common.annotation.Log;
import org.archer.archermq.common.log.LogConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


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
@Component
public class AmqpDecoder extends ChannelInboundHandlerAdapter {


    private final ByteToMessageDecoder decoderDelegate;

    /**
     * 这里最大长度应当是int的无符号最大值，一般情况下，使用该值足矣
     * todo dongyue
     */
    @Value("max.frame.length")
    private int maxFrameLength = Integer.MAX_VALUE;

    @Value("length.field.offset")
    private int lengthFieldOffset = 3;

    @Value("length.field.length")
    private int lengthFieldLength = 4;

    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public AmqpDecoder() {
        decoderDelegate = new LengthFieldBasedFrameDecoder(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }

    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void setSingleDecode(boolean singleDecode) {
        decoderDelegate.setSingleDecode(singleDecode);
    }

    public boolean isSingleDecode() {
        return decoderDelegate.isSingleDecode();
    }

    public void setCumulator(ByteToMessageDecoder.Cumulator cumulator) {
        decoderDelegate.setCumulator(cumulator);
    }

    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void setDiscardAfterReads(int discardAfterReads) {
        decoderDelegate.setDiscardAfterReads(discardAfterReads);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        decoderDelegate.channelRead(ctx, msg);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelReadComplete(ctx);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelInactive(ctx);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        decoderDelegate.userEventTriggered(ctx, evt);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelRegistered(ctx);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelUnregistered(ctx);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelActive(ctx);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelWritabilityChanged(ctx);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        decoderDelegate.exceptionCaught(ctx, cause);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public boolean isSharable() {
        return decoderDelegate.isSharable();
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.handlerAdded(ctx);
    }

    @Override
    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.handlerRemoved(ctx);
    }

    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void setMaxFrameLength(int maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }

    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void setLengthFieldOffset(int lengthFieldOffset) {
        this.lengthFieldOffset = lengthFieldOffset;
    }

    @Log(layer = LogConstants.TRANSPORT_LAYER)
    public void setLengthFieldLength(int lengthFieldLength) {
        this.lengthFieldLength = lengthFieldLength;
    }
}
