package org.archer.archermq.protocol.transport;


import io.netty.buffer.ByteBuf;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * 标准方法帧实现
 * 方法帧可以携带高级协议命令(我们称之为方法(methods)).一个方法帧携带一个命令
 *
 * @author dongyue
 * @date 2020年04月14日13:04:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StandardMethodFrame extends StandardFrame {

    private short rawClassId;

    private short rawMethodId;

    private ByteBuf rawArgs;

    private Map<String, Object> args;
}
