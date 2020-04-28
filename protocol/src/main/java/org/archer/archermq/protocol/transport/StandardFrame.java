package org.archer.archermq.protocol.transport;

import io.netty.buffer.ByteBuf;
import lombok.Data;
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

    protected byte rawType;
    protected FrameTypeEnum frameType;

    protected short rawChannelId;
    protected Channel channel;

    protected io.netty.channel.Channel tcpChannel;

    protected int size;

    protected ByteBuf payload;

    protected byte rawFrameEnd;

    public StandardFrame() {
    }

    public StandardFrame(Frame extendedFrame) {
        this.rawType = extendedFrame.type().getVal();
        this.frameType = extendedFrame.type();
        this.rawChannelId = extendedFrame.channelId();
        this.channel = extendedFrame.channel();
        this.tcpChannel = extendedFrame.tcpChannel();
        this.size = extendedFrame.size();
        this.payload = extendedFrame.content();
        this.rawFrameEnd = extendedFrame.frameEnd();
    }

    @Override
    public FrameTypeEnum type() {
        if (Objects.isNull(frameType)) {
            frameType = FrameTypeEnum.getByVal(rawType);
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

    @Override
    public io.netty.channel.Channel tcpChannel() {
        return tcpChannel;
    }

    public void setRawType(byte rawType) {
        this.rawType = rawType;
        this.frameType = FrameTypeEnum.getByVal(rawType);
    }

    public void setRawChannelId(short rawChannelId) {
        this.rawChannelId = rawChannelId;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setPayload(ByteBuf payload) {
        this.payload = payload;
    }

    public void setRawFrameEnd(byte rawFrameEnd) {
        this.rawFrameEnd = rawFrameEnd;
    }

    public void setTcpChannel(io.netty.channel.Channel tcpChannel){
        this.tcpChannel = tcpChannel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
