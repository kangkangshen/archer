package org.archer.archermq.test;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Data;
import org.archer.archermq.config.cfgcenter.ConfigCenterService;
import org.archer.archermq.protocol.InternalClient;
import org.archer.archermq.protocol.Server;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.constants.FrameTypeEnum;
import org.archer.archermq.protocol.transport.Frame;
import org.archer.archermq.protocol.transport.FrameBuilder;
import org.archer.archermq.protocol.transport.FrameDispatcher;
import org.archer.archermq.protocol.transport.StandardMethodFrame;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;
import java.util.function.IntToDoubleFunction;


@RestController
public class AmqpTest {




    @Autowired
    private FrameDispatcher frameDispatcher;

    @Autowired
    private Server server;

    @GetMapping("/start")
    public String testFullLink() {
        Map<String,Object> args = Maps.newHashMap();
        args.put("reserved-1","");
        String argsJson = JSON.toJSONString(args);
        ByteBuf buf = Unpooled.buffer();
        buf.writeShort(20);
        buf.writeShort(10);
        buf.writeBytes(argsJson.getBytes());
        Frame frame = FrameBuilder.allocateFrame(FrameTypeEnum.METHOD.getVal(),Short.MIN_VALUE,buf);
        Frame returnedFrame = frameDispatcher.dispatchFrame(frame);
        System.out.println("invoked");
        return "success";
    }


    @Autowired
    private ConfigCenterService configCenterService;

    @GetMapping("/config")
    public String testCfgCenter(){
        return "hello,world";

    }


    @Autowired
    private InternalClient internalClient;

    @GetMapping("/connect")
    public String testConnect(){
        internalClient.connect("localhost",5672);
        Map<String,Object> connArgs = Maps.newHashMap();
        connArgs.put("virtual-host","default");
        connArgs.put("reserved-1","");
        connArgs.put("reserved-2","");
        String argsJson = JSON.toJSONString(connArgs);
        ByteBuf buf = Unpooled.buffer();
        buf.writeShort(10);
        buf.writeShort(40);
        buf.writeBytes(argsJson.getBytes());
        Frame frame = FrameBuilder.allocateFrame(FrameTypeEnum.METHOD.getVal(),Short.MIN_VALUE,buf);
        internalClient.send(frame);

        Map<String,Object> channelArgs = Maps.newHashMap();
        channelArgs.put("reserved-1","");
        argsJson = JSON.toJSONString(channelArgs);
        buf = Unpooled.buffer();
        buf.writeShort(20);
        buf.writeShort(10);
        buf.writeBytes(argsJson.getBytes());
        frame = FrameBuilder.allocateFrame(FrameTypeEnum.METHOD.getVal(),Short.MIN_VALUE,buf);
        internalClient.send(frame);
        return "hello,world";
    }

    @GetMapping("/openChannel")
    public String testOpenNewChannel(){
        Map<String,Object> args = Maps.newHashMap();
        args.put("reserved-1","");
        String argsJson = JSON.toJSONString(args);
        ByteBuf buf = Unpooled.buffer();
        buf.writeShort(20);
        buf.writeShort(10);
        buf.writeBytes(argsJson.getBytes());
        Frame frame = FrameBuilder.allocateFrame(FrameTypeEnum.METHOD.getVal(),Short.MIN_VALUE,buf);
        internalClient.send(frame);
        return "hello,world";
    }

}
