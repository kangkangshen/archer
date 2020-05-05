package org.archer.archermq.protocol.transport.handler;

import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.common.log.LogInfo;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.FrameTypeEnum;
import org.archer.archermq.protocol.transport.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 心跳是一种设计用来撤销(undo)TCP/IP功能的技术,
 * 也就是说在长时间超时后，它有能力通过关闭broker物理连接来进行恢复．
 * 在某些情景下，我们需要快速知道节点连接是否断开了，或者是由于什么原因不能响应了.
 * 因为心跳可以在较低水平上进行，我们在传输层次上按节点交换的特定帧类型来处理，而不是按类方法.
 * 应该由客户端一方主动发起心跳
 *
 * @author dongyue
 * @date 2020年04月22日17:11:19
 */
@Component
public class StandardHeartbeatFrameHandler implements FrameHandler {

    @Autowired
    private FrameConverter frameConverter;

    @Override
    public boolean canHandle(FrameTypeEnum targetType) {
        return FrameTypeEnum.HEARTBEAT.equals(targetType);
    }

    @Override
    public boolean validate(Frame targetFrame) {
        if (Objects.equals(targetFrame.channelId(), Frame.HEART_BEAT_CHANNEL_ID)) {
            throw new ConnectionException(ExceptionMessages.ConnectionErrors.FRAME_ERR);
        }
        return true;
    }

    @Override
    public Frame handleFrame(Frame frame) {
        LogInfo logInfo = BizLogUtil.start()
                .setLayer(LogConstants.TRANSPORT_LAYER)
                .setType(LogConstants.HEARTBEAT);
        try {
            //todo dongyue
            StandardHeartbeatFrame ping = frameConverter.convert(frame);
            logInfo.addContent(LogConstants.TIME_STAMP, String.valueOf(ping.getCreatedTime()));
            StandardHeartbeatFrame pong = new StandardHeartbeatFrame(frame);
            pong.setCreatedTime(System.currentTimeMillis());
            return pong;

        } finally {
            BizLogUtil.end();
        }


    }
}
