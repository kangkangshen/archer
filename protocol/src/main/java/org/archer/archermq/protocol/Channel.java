package org.archer.archermq.protocol;


import com.google.common.collect.Sets;
import org.archer.archermq.protocol.constants.StateEnum;
import org.archer.archermq.protocol.model.Command;
import org.archer.archermq.protocol.transport.Frame;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

/**
 * 多路复用连接中的一条独立的双向数据流通道。为会话提供物理传输介质。
 * 其生命周期设计如下：
 * 1. client打开一个新通道(Open).
 * 2. server确认新通道准备就绪(Open-Ok).
 * 3. client和server按预期来使用通道.
 * 4. 一个节点(client或server) 关闭了通道(Close).
 * 5. 另一个节点对通道关闭进行握手(Close-Ok).
 * 6.节点不应该允许一个非常繁忙的通道让一个不太繁忙的通道饿死.
 *
 * @author dongyue
 * @date 2020年04月13日21:22:46
 */
public interface Channel extends LifeCycle {

    /**
     * 返回当前channelId,默认情况下，id == name
     * 一般情况下，应当返回无业务语义的ID字段用于检索channel
     *
     * @return 当前channel的Id
     */
    default String id() {
        return name();
    }

    /**
     * 返回当前channelName
     *
     * @return 当前channel的名称
     */
    String name();

    Connection conn();

    /**
     * 开启/关闭流模式
     *
     * @param active 是否开启/关闭流模式
     */
    void setFlow(boolean active);


    /**
     * 关闭当前channel
     */
    void close();

    /**
     * 返回客户端正在消费，尚未回复consumeOk的消息集合
     *
     * @return 客户端正在消费，尚未回复consumeOk的消息集合，默认返回emptySet
     */
    default Set<MessageQueue> consuming() {
        return Sets.newHashSet();
    }


    /**
     * 获取当前channel最后声明的一个消息队列
     *
     * @return 当前channel最后声明的一个消息队列
     */
    MessageQueue lastDeclareMsgQueue();

    /**
     * 设置qos
     *
     * @param prefetchSize  预取帧大小
     * @param prefetchCount 预取帧个数
     */
    void qos(int prefetchSize, short prefetchCount);

    /**
     * 返回当前channel的调用栈
     *
     * @return 当前channel的调用栈，默认是emptyStack
     */
    default Stack<Command<?>> invokeStack() {
        return new Stack<>();
    }

    /*
     * 弹出当前channel队列的最先一个帧
     */
    Frame poll();

    /**
     * 压入当前帧
     *
     * @param frame 当前帧
     */
    void offer(Frame frame);

    /**
     * 返回当前channel发送队列的队头帧
     *
     * @return 当前channel发送队列的队头帧
     */
    Frame peak();

    List<Message> unConfirmedMsg();

    void confirmMsg(String deliveryTag, boolean multiple);

    void confirmAllMsg();

    void exchange(Message msg);

    void redeliver(Message msg);

    void beginTx();

    void commit();

    void rollback();
}
