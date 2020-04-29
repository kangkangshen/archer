package org.archer.archermq.protocol.transport.impl.exchange;

import org.archer.archermq.protocol.Message;
import org.archer.archermq.protocol.MessageQueue;
import org.archer.archermq.protocol.constants.ExchangeTypeEnum;
import org.archer.archermq.protocol.transport.BaseExchange;

/**
 * topic交换器类型按如下方式来工作:
 * 1. 消息队列使用路由模式P来绑定到交换器．
 * 2. 发布者使用路由键R来向交换器发送消息．
 * 3. 当R匹配P时，消息将被传递到消息队列．
 * 用于topic交换器的路由键必须由０个或多个由点号
 * 用于topic交换器的路由键必须由点分隔的零或多个单词组成.每个单词必须包含字母A-Z和a-z 以及数字0-9.
 * 路由模式与路由键遵循相同的规则，* 用于匹配单个单词，# 用于匹配０个或多个单词.因此路由模式*.stock.# 会匹配路由键usd.stock 和eur.stock.db 但不匹配stock.nasdaq.
 * 对于topic交换器我们建议的设计是保持所有已知路由键的集合，当发布者使用了新的路由键时，才更新此集合. 通过给定一个路由键来确实所有绑定是可能的,因此可为消息快速找到消息队列. 此交换器类型是可选的.
 * server应该实现topic交换器类型，在这种情况下，server 必须在每个虚拟主机中预先定义至少一个 topic交换器，其名称为amq.topic.
 *
 * @author dongyue
 * @date 2020年04月28日22:33:25
 */
public class TopicExchange extends BaseExchange {
    public TopicExchange(String name, String exchangeId, boolean durable) {
        super(name, ExchangeTypeEnum.TOPIC.getVal(), exchangeId, durable);
    }


    @Override
    protected void exchangeMsgInternal(Message message, String routingKey) {

    }
}
