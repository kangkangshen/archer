package org.archer.archermq.protocol;

/**
 * 一个命名实体，用来保存消息直到发送给消费者。
 * 消息队列保存消息，并将消息分发给一个或多个订阅客户端。
 *
 * @author dongyue
 * @date 2020年04月13日21:24:19
 */
public interface MessageQueue extends LifeCycle {

    /**
     * 获取当前消息队列所绑定的交换器，按照AMQP协议，mq和exchange之间是一对多的关系
     *
     * @return 当前所绑定的交换器
     */
    Exchange exchange();

    String bindingKey();

    /**
     * 获取当前队列名
     * 队列名必须包含1到255个字符。
     * 队列名首字符限定为字母a-z或者A-Z，数字0-9，或者下划线‘_’；接下来的其他字符必须是合法的UTF-8字符。
     *
     * @return 当前队列名
     */
    String name();

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
    default boolean autoDelete(){
        return false;
    }

}
