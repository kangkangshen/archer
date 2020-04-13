package org.archer.archermq.protocol.transport;

import io.netty.buffer.ByteBuf;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.constants.FrameTypeEnum;

public class StandardFrame implements Frame{

    private byte rawType;
    private FrameTypeEnum frameType;

    private short rawChannelId;
    private Channel channel;

    private long size;

    private ByteBuf payload;



    @Override
    public FrameTypeEnum type() {
        return FrameTypeEnum.getByVal((int) rawType);
    }

    @Override
    public short channelId() {
        return rawChannelId;
    }

    @Override
    public Channel channel() {
        return channel;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public ByteBuf content() {
        return payload;
    }
}
