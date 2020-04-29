package org.archer.archermq.protocol.transport.impl.exchange;

import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.MessageQueue;
import org.archer.archermq.protocol.constants.ExchangeTypeEnum;
import org.archer.archermq.protocol.transport.BaseExchange;


/**
 * headers交换器类型按如下方式进行工作:
 * 1. 消息队列使用包含匹配绑定和带有默认值的header参数表来绑定交换器.在这种交换器类型中，不使用路由键.
 * 2.发布者向交换器发送消息，这些消息的headers属性中包含名称－值对的表.
 * 3.如果消息头属性与队列绑定的参数相匹配，则消息传递给队列。
 * 匹配算法是由参数表中的名称值对这样的特殊绑定参数来控制的. 这个参数的名称是'x-match'.
 * 它可以接受两种值， 以表示表格中其它的名称值对将如何来进行匹配：
 *  'all' 则表明所有其它的名称值对必须与路由消息的头属性相匹配(即.AND匹配)
 *  'any' 则表明只要消息头属性中的任何一个字段匹配参数表中的字段，则消息就应该被路由(即. OR匹配)．
 * 绑定参数中的字段必须与消息字段中的字段相匹配，这些情况包括：如果绑定参数中的字段没有值且在消息头中存在相同名称的字段，或者绑定参数中的字段有值，且消息属性中存在同样的字段且有相同的值。
 * 任何以'x-'而不是'x-match'开头的字段为将来保留使用并会被忽略．
 * server应该实现headers交换器类型, 且server必须在每个虚拟主机中预先声明至少一个headers交换器,且名称为amq.match.
 *
 * @author dongyue
 * @date 2020年04月28日22:33:52
 */
public class HeadersExchange extends BaseExchange {
    public HeadersExchange(String name,String exchangeId, boolean durable) {
        super(name, ExchangeTypeEnum.HEADERS.getVal(), exchangeId, durable);
    }

    @Override
    protected void exchangeMsgInternal(Message message, String routingKey) {

    }
}
