package org.archer.archermq.protocol.transport.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.archer.archermq.protocol.constants.FrameTypeEnum;
import org.archer.archermq.protocol.transport.Frame;
import org.archer.archermq.protocol.transport.FrameBuilder;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class AmqpDecoderTest {

    @Test
    public void decode() {

        AmqpDecoder decoder = new AmqpDecoder();
        AmqpEncoder encoder = new AmqpEncoder();
        Map<String,Object> args = Maps.newHashMap();
        args.put("reserved-1","");
        String argsJson = JSON.toJSONString(args);
        ByteBuf buf = Unpooled.buffer();
        buf.writeShort(20);
        buf.writeShort(10);
        buf.writeBytes(argsJson.getBytes());
        Frame frame = FrameBuilder.allocateFrame(FrameTypeEnum.METHOD.getVal(),Short.MIN_VALUE,buf);
    }
}