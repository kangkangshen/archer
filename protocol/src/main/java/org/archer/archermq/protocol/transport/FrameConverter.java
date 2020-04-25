package org.archer.archermq.protocol.transport;

public interface FrameConverter {

    <T> T convert(Frame frame);
}
