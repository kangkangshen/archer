package org.archer.archermq.protocol.transport.handler;

import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.Server;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FrameTypeEnum;
import org.archer.archermq.protocol.transport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;


/**
 * 内容帧是附着在方法帧之后进行的，如如Basic.Publish, Basic.Deliver等等
 * 当一个节点发送像这样的方法帧时，它总是会遵循一个内容头帧(content header frame)和零个或多个内容体帧(content body frame)的形式.
 * <p>
 * 因为其上的缘故，我们发出一个方法帧到服务端之后，往往我们需要保证下次的内容帧请求也是在同一台机器上完成响应，即我们需要session
 * <p>
 * 因为我们实现了channel，需要关注channel之间的跨线程特性
 *
 * @author dongyue
 * @date 2020年04月22日17:12:45
 */
@Component
public class StandardContentFrameHandler extends BaseFrameHandler {


    private Frame lastContentHeader;

    @Override
    public boolean canHandle(FrameTypeEnum targetType) {
        return FrameTypeEnum.CONTENT_BODY.equals(targetType) || FrameTypeEnum.CONTENT_HEADER.equals(targetType);
    }

    @Override
    public boolean validate(Frame targetFrame) {
        //double check
        boolean valid = true;
        if (!Objects.nonNull(targetFrame)) {
            return false;
        }
        valid = Objects.equals(FrameTypeEnum.CONTENT_BODY, targetFrame.type()) || Objects.equals(FrameTypeEnum.CONTENT_HEADER, targetFrame.type());

        valid &= (Objects.isNull(lastContentHeader) && Objects.equals(FrameTypeEnum.CONTENT_HEADER, targetFrame.type())) ||
                (Objects.nonNull(lastContentHeader) && Objects.equals(FrameTypeEnum.CONTENT_BODY, targetFrame.type()));
        return valid;

    }

    @Override
    protected Frame handleFrameInternal(Frame frame) {
        switch (frame.type()) {
            case CONTENT_HEADER:
                lastContentHeader = frame;
                break;
            case CONTENT_BODY:
                Channel amqpChannel = frame.channel();
                amqpChannel.exchange(MessageBuilder.buildMsg(lastContentHeader,frame));
                break;
            default:
                throw new ConnectionException(ExceptionMessages.SystemErrors.STATE_ERR);
        }
        return null;
    }
}
