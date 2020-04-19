package org.archer.archermq.protocol.transport;

/**
 * 帧分派器，前台收集到所有有效帧，由帧分派器根据特定规则分派给特定的帧处理器进行处理
 */
public interface FrameDispatcher {



    void dispatchFrame(Frame rawFrame);


}
