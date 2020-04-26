package org.archer.archermq.protocol.transport.handler;

import org.archer.archermq.protocol.constants.FrameTypeEnum;
import org.archer.archermq.protocol.transport.Frame;
import org.archer.archermq.protocol.transport.FrameHandler;
import org.springframework.stereotype.Component;


/**
 * 内容帧是附着在方法帧之后进行的，如如Basic.Publish, Basic.Deliver等等
 * 当一个节点发送像这样的方法帧时，它总是会遵循一个内容头帧(content header frame)和零个或多个内容体帧(content body frame)的形式.
 *
 * 因为其上的缘故，我们发出一个方法帧到服务端之后，往往我们需要保证下次的内容帧请求也是在同一台机器上完成响应，即我们需要session
 *
 * 因为我们实现了channel，需要关注channel之间的跨线程特性
 *
 * @author dongyue
 * @date 2020年04月22日17:12:45
 */
@Component
public class StandardContentFrameHandler implements FrameHandler {
    @Override
    public boolean canHandle(FrameTypeEnum targetType) {
        return FrameTypeEnum.CONTENT_BODY.equals(targetType) || FrameTypeEnum.CONTENT_HEADER.equals(targetType);
    }

    @Override
    public boolean validate(Frame targetFrame) {
        return false;
    }

    @Override
    public Frame handleFrame(Frame frame) {
        return null;
    }
}
