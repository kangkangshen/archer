package org.archer.archermq.protocol.transport;

import org.archer.archermq.protocol.constants.ExceptionMessages;

/**
 * 通道异常定义
 * 任何操作错误(未找到消息队列，访问权限不足)都会导致一个通道异常
 *
 * @author dongyue
 * @date 2020年04月14日13:17:45
 */
public class ChannelException extends RuntimeException {

    private final ExceptionMessages exceptionMessages;

    public ChannelException(ExceptionMessages exceptionMessages){
        this.exceptionMessages = exceptionMessages;
    }

    public ExceptionMessages getExceptionMessages() {
        return exceptionMessages;
    }
}
