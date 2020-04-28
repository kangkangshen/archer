package org.archer.archermq.test;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.archer.archermq.protocol.constants.FeatureKeys;
import org.archer.archermq.protocol.constants.FrameTypeEnum;
import org.archer.archermq.protocol.transport.Frame;
import org.archer.archermq.protocol.transport.FrameBuilder;
import org.archer.archermq.protocol.transport.FrameDispatcher;
import org.archer.archermq.protocol.transport.StandardMethodFrame;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
public class AmqpTest {




    @Autowired
    private FrameDispatcher frameDispatcher;

    @GetMapping("/start")
    public String testFullLink() {

        StandardMethodFrame methodFrame = new StandardMethodFrame();

        methodFrame.setRawClassId((short) 20);
        methodFrame.setRawMethodId((short) 10);
        methodFrame.setTcpChannel(null);
        Map<String,Object> args = Maps.newHashMap();
        args.put("reserved-1","");
        String argsJson = JSON.toJSONString(args);
        int size = argsJson.getBytes().length;
        methodFrame.setRawArgs(argsJson.getBytes());
        methodFrame.setArgs(args);
        Frame returnedFrame = frameDispatcher.dispatchFrame(methodFrame);
        System.out.println("invoked");
        return "success";
    }


}
