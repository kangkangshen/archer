package org.archer.archermq.protocol;


import io.netty.buffer.ByteBuf;

/**
 * 消息体,标记接口
 *
 * @author dongyue
 * @date 2020年04月13日21:46:56
 */
public interface MessageBody {
    ByteBuf content();
}
