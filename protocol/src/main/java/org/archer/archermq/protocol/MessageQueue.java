package org.archer.archermq.protocol;

import org.archer.archermq.protocol.constants.DeliverMode;

import java.util.Map;

/**
 * 一个命名实体，用来保存消息直到发送给消费者。
 * 消息队列保存消息，并将消息分发给一个或多个订阅客户端。
 * 持久化的消息队列生命周期如下：
 * 1. client断言消息队列存在(Declare, 使用"passive"参数).
 * 2. server确认消息队列存在(Declare-Ok).
 * 3. client从消息队列中读取消息。
 * 临时消息队列的生命周期设计如下：
 * 1. client创建消息队列(Declare,不提供队列名称，服务器会分配一个名称). server 确认(Declare-Ok).
 * 2. client 在消息队列上启动一个消费者. 消费者的精确功能是由Basic类定义的。
 * 3. client 取消消费者, 要么是显示取消，要么是通过关闭通道／连接隐式取消的
 * 4. 当最后一个消费者从消息队列中消失的时候，在过了礼貌性超时后，server会删除消息队列
 *
 * @author dongyue
 * @date 2020年04月13日21:24:19
 */
public interface MessageQueue extends LifeCycle {

    void setDeliverMode(DeliverMode deliverMode);

    /**
     * 标准消息队列前辍是"amq."
     */
    String STANDARD_MESSAGE_QUEUE_NAME_PREFIX = "amq.";

    String bindingKey();

    void bind(Exchange exchange, String routingKey, Map<String, Object> args);

    void unBind(Exchange exchange, String routingKey, Map<String, Object> args);

    int purge();

    void lock();

    void unlock();

    void enqueue(Message message);

    Message dequeue();

    /**
     * 获取当前队列名
     * 队列名必须包含1到255个字符。
     * 队列名首字符限定为字母a-z或者A-Z，数字0-9，或者下划线‘_’；接下来的其他字符必须是合法的UTF-8字符。
     *
     * @return 当前队列名
     */
    String name();

    /**
     * 获取当前队列里面消息的个数，注意队列存储是分布式的，消息总数代表全体队列中的消息总数
     * 该值是一个瞬时值。
     *
     * @return 当前消息队列里面的消息总数
     */
    int msgCount();

    int localMsgCnt();

    /**
     * 获取当前队列监听的消费者总数，注意，队列监听是分布式的，消费者总数代表全体队列中的消费者总数
     * 该值是一个瞬时值
     *
     * @return 监听当前消息队列的消费者总数
     */
    int consumerCount();

    Exchange exchange();

    Registrar<String,Consumer> getConsumerRegistry();

    /**
     * 判断当前队列是不是持久队列
     * 如果指定这个属性，消息队列会在服务器重启之后仍然能够继续工作
     * 默认为true
     *
     * @return 当前队列是否是持久队列
     */
    default boolean durable() {
        return true;
    }

    /**
     * 判断当前是不是
     * 如果指定这个属性，当所有客户端不再使用这个队列时，服务器会立即或者不久之后把这个队列删除掉。
     * 默认为false
     *
     * @return 当前队列是否可以自动删除
     */
    default boolean autoDelete() {
        return false;
    }

    default boolean exclusive() {
        return false;
    }

}
