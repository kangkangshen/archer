package org.archer.archermq.test;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.archer.archermq.protocol.constants.FrameTypeEnum;
import org.archer.archermq.protocol.transport.Frame;
import org.archer.archermq.protocol.transport.FrameBuilder;
import org.archer.archermq.protocol.transport.FrameDispatcher;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AmqpTest {




    @Autowired
    private FrameDispatcher frameDispatcher;

    @GetMapping("/start")
    public String testFullLink() {

        String payload = "wukang";
        int size = payload.getBytes().length;
        ByteBuf byteBuf = Unpooled.buffer(size);
        byteBuf.writeBytes(payload.getBytes());
        Frame methodFrame = FrameBuilder.allocateFrame(FrameTypeEnum.METHOD.getVal(), (short) 1, size, byteBuf);
        Frame returnedFrame = frameDispatcher.dispatchFrame(methodFrame);
        System.out.println("invoked");
        return "success";
    }


}
