package org.archer.archermq.protocol.transport.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.archer.archermq.protocol.constants.FrameTypeEnum;
import org.archer.archermq.protocol.model.Command;
import org.archer.archermq.protocol.model.MethodResolver;
import org.archer.archermq.protocol.transport.Frame;
import org.archer.archermq.protocol.transport.FrameConverter;
import org.archer.archermq.protocol.transport.FrameHandler;
import org.archer.archermq.protocol.transport.StandardMethodFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;


/**
 * 标准方法帧处理类，一个方法帧的处理流程是：
 * 1. 读取方法帧负载.
 * 2. 将其拆包成结构. 方法通常有相同的结构，因此我们可以快速对方法进行拆包．
 * 3. 检查在当前上下文中是否允许出现方法．
 * 4. 检查方法参数是否有效.
 * 5.执行方法.
 *
 * @author dongyue
 * @date 2020年04月15日17:17:27
 */
@Component
public class StandardMethodFrameHandler implements FrameHandler {

    @Autowired
    private MethodResolver methodResolver;

    @Autowired
    private FrameConverter frameConverter;




    @Override
    public boolean canHandle(FrameTypeEnum targetType) {
        return FrameTypeEnum.METHOD.equals(targetType);
    }

    @Override
    public boolean validate(Frame targetFrame) {
        //double check
        if(!Objects.nonNull(targetFrame)){
            return false;
        }
        if(!Objects.equals(FrameTypeEnum.METHOD, targetFrame.type())){
            return false;
        }

        return true;
    }

    @Override
    public Object handleFrame(Frame frame) {

        StandardMethodFrame methodFrame = frameConverter.convert(frame);

        if(!methodResolver.support(methodFrame.getRawClassId(),methodFrame.getRawMethodId())){
            return false;
        }

        Command<?> targetCommand = methodResolver.route(methodFrame);

        return targetCommand.execute();


    }


}
