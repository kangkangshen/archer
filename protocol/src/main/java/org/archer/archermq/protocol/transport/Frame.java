package org.archer.archermq.protocol.transport;


import io.netty.buffer.ByteBuf;
import org.archer.archermq.protocol.Channel;
import org.archer.archermq.protocol.constants.FrameTypeEnum;

import java.io.Serializable;

/**
 * 通用帧接口定义
 *
 * @author dongyue
 * @date 2020年04月13日23:06:00
 */
public interface Frame extends Serializable {

    /**
     * 获取当前数据帧类型
     *
     * @return 当前数据帧类型, 必不为空
     * @see FrameTypeEnum
     */
    FrameTypeEnum type();

    /**
     * 获取当前帧所属的channelId
     *
     * @return 当前帧所属的channelId
     */
    short channelId();

    /**
     * 获取当前帧所属的channel,使用channelId()进行查询
     *
     * @return 当前帧所属的channel
     */
    Channel channel();

    /**
     * 获取当前帧的载荷长度
     *
     * @return 当前帧的载荷长度
     */
    int size();

    /**
     * 获取当前帧的内容
     *
     * @return 当前帧的内容，以字节形式展示
     */
    ByteBuf content();

    byte frameEnd();
}
