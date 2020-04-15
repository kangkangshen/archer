package org.archer.archermq.protocol;


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



}
