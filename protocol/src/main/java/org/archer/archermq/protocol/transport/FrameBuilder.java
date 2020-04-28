package org.archer.archermq.protocol.transport;


import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

/**
 * 帧构造器帮助类
 *
 * @author dongyue
 * @date 2020年04月14日16:42:40
 */
public class FrameBuilder {

    public static Frame allocateFrame(byte rawType, short channelId, int size, ByteBuf payload, Channel tcpChannel) {
        StandardFrame frame = new StandardFrame();
        frame.setRawType(rawType);
        frame.setRawChannelId(channelId);
        frame.setSize(size);
        frame.setPayload(payload);
        frame.setRawFrameEnd(Frame.FRAME_END);
        frame.setTcpChannel(tcpChannel);
        return frame;
    }

    public static Frame allocateFrame(byte rawType, short channelId, int size, ByteBuf payload) {
        return allocateFrame(rawType, channelId, size, payload, null);
    }
}
