package org.archer.archermq.protocol.transport;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.buffer.UnpooledDirectByteBuf;
import lombok.SneakyThrows;
import org.apache.commons.lang3.CharSet;
import org.apache.commons.lang3.StringUtils;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
public class DefaultFrameConverter implements FrameConverter{

    @Override
    @SuppressWarnings("unchecked")
    public <T> T convert(Frame frame) {
        switch (frame.type()){
            case METHOD:
                return (T) buildMethodFrame(frame);
            case HEARTBEAT:
                return (T) buildHeartbeatFrame(frame);
            case CONTENT_BODY:
                return (T) buildContentBodyFrame(frame);
            case CONTENT_HEADER:
                return (T) buildContentHeaderFrame(frame);
            default:
                throw new ConnectionException(ExceptionMessages.ConnectionErrors.UNEXPECTED_FRAME);
        }
    }
    private StandardMethodFrame buildMethodFrame(Frame rawFrame){
        ByteBuf payload = rawFrame.content();

        short classId = payload.readShort();
        short methodId = payload.readShort();

        byte[] rawArgBytes = new byte[payload.readableBytes()];
        payload.readBytes(rawArgBytes);

        Map<String,Object> args = JSON.parseObject(new String(rawArgBytes, StandardCharsets.UTF_8));

        StandardMethodFrame methodFrame = new StandardMethodFrame(rawFrame);
        methodFrame.setRawClassId(classId);
        methodFrame.setRawMethodId(methodId);
        methodFrame.setRawArgs(rawArgBytes);
        methodFrame.setArgs(args);
        return methodFrame;
    }

    private StandardContentFrame.StandardContentHeaderFrame buildContentHeaderFrame(Frame rawFrame){
        ByteBuf payload = rawFrame.content();
        short classId = payload.readShort();
        short weight = payload.readShort();
        long bodySize = payload.readLong();
        short propertyFlags = payload.readShort();
        byte[] rawProperties = new byte[payload.readableBytes()];
        payload.writeBytes(rawProperties);
        Map<String,Object> properties = JSON.parseObject(new String(rawProperties,StandardCharsets.UTF_8));
        StandardContentFrame.StandardContentHeaderFrame headerFrame = new StandardContentFrame.StandardContentHeaderFrame(rawFrame);
        headerFrame.setRawClassId(classId);
        headerFrame.setWeight(weight);
        headerFrame.setBodySize(bodySize);
        headerFrame.setPropertyFlags(propertyFlags);
        headerFrame.setProperties(properties);
        return headerFrame;

    }

    private StandardContentFrame.StandardContentBodyFrame buildContentBodyFrame(Frame rawFrame){
        StandardContentFrame.StandardContentBodyFrame bodyFrame = new StandardContentFrame.StandardContentBodyFrame(rawFrame);
        ByteBuf payload = rawFrame.content().copy(0,rawFrame.content().readableBytes()-2);
        byte frameEnd = rawFrame.content().skipBytes(payload.readableBytes()).readByte();
        bodyFrame.setBinPayload(payload);
        bodyFrame.setFrameEnd(frameEnd);
        return bodyFrame;
    }

    private StandardHeartbeatFrame buildHeartbeatFrame(Frame rawFrame){
        StandardHeartbeatFrame heartbeatFrame = new StandardHeartbeatFrame(rawFrame);
        ByteBuf payload = rawFrame.content();
        heartbeatFrame.setCreatedTime(payload.readLong());

        return heartbeatFrame;

    }
}
