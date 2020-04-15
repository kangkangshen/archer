package org.archer.archermq.protocol.transport;

/**
 * 用于解析特定的帧，如方法帧，内容帧，心跳帧等
 * @param <T>
 */
public interface FrameHandler<T extends Frame> {

    Object handleFrame(T frame);

}
