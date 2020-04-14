package org.archer.archermq.protocol.transport.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/***
 * 最初进入的字节解码器，用于将字节转换成帧对象.
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

    public AmqpDecoder(){
        BizLogUtil.start()
                .setLayer(LogConstants.TRANSPORT_LAYER)
                .setType(LogConstants.INSTANCE_CREATED)
                .setContent(LengthFieldBasedFrameDecoder.class.getName());

        decoderDelegate = new LengthFieldBasedFrameDecoder(maxFrameLength,lengthFieldOffset,lengthFieldLength);

        BizLogUtil.end();
    }

    public void setSingleDecode(boolean singleDecode) {
        /*
          未来配置项变更需要使用切面的方式替代
         */
        BizLogUtil.start()
                .setLayer(LogConstants.TRANSPORT_LAYER)
                .setType(LogConstants.CONFIG_CHANGED)
                .setContent("singleDecode changed");

        decoderDelegate.setSingleDecode(singleDecode);

        BizLogUtil.end();
    }

    public boolean isSingleDecode() {
        return decoderDelegate.isSingleDecode();
    }

    public void setCumulator(ByteToMessageDecoder.Cumulator cumulator) {
        BizLogUtil.start()
                .setLayer(LogConstants.TRANSPORT_LAYER)
                .setType(LogConstants.CONFIG_CHANGED)
                .setContent("cumulator changed");

        decoderDelegate.setCumulator(cumulator);

        BizLogUtil.end();
    }

    public void setDiscardAfterReads(int discardAfterReads) {
        BizLogUtil.start()
                .setLayer(LogConstants.TRANSPORT_LAYER)
                .setType(LogConstants.CONFIG_CHANGED)
                .setContent("discardAfterReads changed");

        decoderDelegate.setDiscardAfterReads(discardAfterReads);

        BizLogUtil.end();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        BizLogUtil.start()
                .setLayer(LogConstants.TRANSPORT_LAYER)
                .setType(LogConstants.METHOD_INVOKE)
                .setContent("channelRead invoked");

        decoderDelegate.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        decoderDelegate.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelActive(ctx);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.channelWritabilityChanged(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        decoderDelegate.exceptionCaught(ctx, cause);
    }

    @Override
    public boolean isSharable() {
        return decoderDelegate.isSharable();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        decoderDelegate.handlerRemoved(ctx);
    }
}
