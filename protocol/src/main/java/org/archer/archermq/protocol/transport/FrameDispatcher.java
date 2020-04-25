package org.archer.archermq.protocol.transport;

import io.netty.channel.ChannelInboundHandler;

/**
 * 帧分派器，前台收集到所有有效帧，由帧分派器根据特定规则分派给特定的帧处理器进行处理
 */
public interface FrameDispatcher extends ChannelInboundHandler {


    /**
     * 根据原始帧的数据要求对帧进行分派
     *
     * @param rawFrame 解码后的原始帧
     * @implNote 一个rawFrame是经历解码后的，值不为空的，符合frame的基本构造的！对于不能处理的帧，将会记录日志后丢弃掉
     */
    void dispatchFrame(Frame rawFrame);




}
