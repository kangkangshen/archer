package org.archer.archermq.protocol.transport.handler;

import org.archer.archermq.protocol.constants.FrameTypeEnum;
import org.archer.archermq.protocol.transport.Frame;
import org.archer.archermq.protocol.transport.FrameHandler;

public class StandardContentBodyFrameHandler implements FrameHandler {
    @Override
    public boolean canHandle(FrameTypeEnum targetType) {
        return false;
    }

    @Override
    public boolean validate(Frame targetFrame) {
        return false;
    }

    @Override
    public Object handleFrame(Frame frame) {
        return null;
    }
}
