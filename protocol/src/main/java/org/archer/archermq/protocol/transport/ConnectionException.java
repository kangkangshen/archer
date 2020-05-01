package org.archer.archermq.protocol.transport;


import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.model.Connection;

/**
 * 连接异常定义
 * 任何结构化的错误(无效参数，坏序列的方法.)都会导致一个连接异常
 *
 * @author dongyue
 * @date 2020年04月14日13:18:54
 */
public class ConnectionException extends RuntimeException {

    private final ExceptionMessages exceptionMessages;

    public ConnectionException(ExceptionMessages exceptionMessages){
        this.exceptionMessages = exceptionMessages;
    }

    public ExceptionMessages getExceptionMessages() {
        return exceptionMessages;
    }

    @Override
    public String getMessage() {
        return ExceptionMessages.buildExceptionMsgWithTemplate("err id #,name #,type #,desc #.",String.valueOf(exceptionMessages.getId()),exceptionMessages.getName(),String.valueOf(exceptionMessages.getErrType()),exceptionMessages.getDesc());
    }
}
