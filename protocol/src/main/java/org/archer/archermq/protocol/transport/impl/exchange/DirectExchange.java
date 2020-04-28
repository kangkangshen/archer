package org.archer.archermq.protocol.transport.impl.exchange;

import org.archer.archermq.common.log.BizLogUtil;
import org.archer.archermq.common.log.LogConstants;
import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.MessageQueue;
import org.archer.archermq.protocol.constants.ExceptionMessages;
import org.archer.archermq.protocol.constants.ExchangeTypeEnum;
import org.archer.archermq.protocol.transport.BaseExchange;
import org.archer.archermq.protocol.transport.ConnectionException;

import java.util.List;


/**
 * direct 交换器按如下方式来工作:
 * 1. 消息队列使用路由键K来绑定交换器．
 * 2. 发布者使用路由键R来向交换器发送消息．
 * 3. 在Ｋ＝Ｒ时，消息会传递到消息队列中.
 * server必须实现direct交换器，并且在每个虚拟主机中必须预定义两个direct交换器: 一个名为 amq.direct, 另一个无公共名称（为Publish方法的默认交换器）.
 * 注意，消息队列可以使用任何有效的路由键值进行绑定,但通常消息队列使用它们自己的名称作路由键来绑定.
 * 事实上，所有消息队列必须能使用其自身队列名称作路由键自动绑定无名称的交换器上．
 *
 * @author dongyue
 * @date 2020年04月28日22:32:20
 */
public class DirectExchange extends BaseExchange {


    public DirectExchange(String name, String exchangeId, boolean durable) {
        super(name, ExchangeTypeEnum.DIRECT.getVal(), exchangeId, durable);
    }

    @Override
    public void acceptMsgQueueBinding(MessageQueue msgQueue, String routingKey) {
        if(msgQueueRegistry.contains(routingKey)&&msgQueueRegistry.get(routingKey).size()>0){
            throw new UnsupportedOperationException(ExceptionMessages.buildExceptionMsgWithTemplate("duplicated register not allow in this exchange #,type #",name(),type().getDesc()));
        }else{
            super.acceptMsgQueueBinding(msgQueue, routingKey);
        }

    }

    @Override
    protected void exchangeMsgInternal(Message message, String routingKey) {
        List<MessageQueue> allMsgQueue = msgQueueRegistry.get(routingKey);
        for(MessageQueue messageQueue:allMsgQueue){
            messageQueue.enqueue(message);
        }
    }
}
