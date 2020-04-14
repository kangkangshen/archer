package org.archer.archermq.protocol.transport;

import io.netty.buffer.ByteBuf;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.constants.FrameTypeEnum;

import java.util.Objects;

/**
 * 标准帧实现
 *
 * @author dongyue
 * @date 2020年04月14日12:49:26
 */
public class StandardFrame implements Frame {

//    private final long ser

    private byte rawType;
    private FrameTypeEnum frameType;

    private short rawChannelId;
    private Channel channel;

    private int size;

    private ByteBuf payload;

    private byte rawFrameEnd;


    @Override
    public FrameTypeEnum type() {
        if(Objects.isNull(frameType)){
            FrameTypeEnum.getByVal((int) rawType);
        }
        return frameType;
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
    public int size() {
        return size;
    }

    @Override
    public ByteBuf content() {
        return payload;
    }

    @Override
    public byte frameEnd() {
        return rawFrameEnd;
    }


}
