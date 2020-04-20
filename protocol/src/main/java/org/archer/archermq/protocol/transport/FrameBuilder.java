package org.archer.archermq.protocol.transport;


import io.netty.buffer.ByteBuf;

/**
 * 帧构造器帮助类
 *
 * @author dongyue
 * @date 2020年04月14日16:42:40
 */
public class FrameBuilder {

    public static Frame allocateFrame(byte rawType, short channelId, int size, ByteBuf payload, byte frameEnd) {
        StandardFrame frame = new StandardFrame();
        frame.setRawType(rawType);
        frame.setRawChannelId(channelId);
        frame.setSize(size);
        frame.setPayload(payload);
        frame.setRawFrameEnd(frameEnd);
        return frame;
    }


}
