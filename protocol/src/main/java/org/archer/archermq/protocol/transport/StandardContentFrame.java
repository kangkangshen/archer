package org.archer.archermq.protocol.transport;


import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 标准内容帧实现
 * 内容是我们通常AMQP服务器在客户端与客户端之间传送和应用数据.
 * 粗略地说，内容是由一组属性加上一个二进制数据部分组成的.
 * 它所允许的属性集合由Basic类定义，而这些属性的形式为内容头帧(content header frame).
 * 其数据可以是任何大小，也有可能被分解成几个（或多个）块，每一个都有内容体帧(content body frame).
 * 当一个节点发送像这样的方法帧时，它总是会遵循一个内容头帧(content header frame)和零个或多个内容体帧(content body frame)的形式.
 * 注意：节点必须要能将分成多个帧的内容体作为单一集合进行存储处理
 * @author dongyue
 * @date 2020年04月14日12:59:07
 */
@Data
public class StandardContentFrame extends StandardFrame{

    StandardContentHeaderFrame headerFrame;

    List<StandardContentBodyFrame> bodyFrames;


    /**
     * 标准内容头帧实现
     *
     */
    @Data
    public static class StandardContentHeaderFrame extends StandardFrame{

        private short rawClassId;

        private short weight;

        private long bodySize;

        private short rawPropertyFlags;

        private Map<String,Object> feature;


    }

    /**
     * 标准内容体帧实现
     */
    @Data
    public static class StandardContentBodyFrame extends StandardFrame{

        private ByteBuf binPayload;

        private byte frameEnd;



    }
}
