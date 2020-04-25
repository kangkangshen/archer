package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.springframework.stereotype.Component;

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
        return null;
    }

    private StandardContentFrame.StandardContentHeaderFrame buildContentHeaderFrame(Frame rawFrame){
        return null;
    }

    private StandardContentFrame.StandardContentBodyFrame buildContentBodyFrame(Frame rawFrame){
        return null;
    }

    private StandardHeartbeatFrame buildHeartbeatFrame(Frame rawFrame){
        return null;
    }
}
