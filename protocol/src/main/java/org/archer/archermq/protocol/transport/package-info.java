/**
 * AMQP传输层实现，用于提供帧处理、信道复用、错误检测和数据表示
 *
 * 其基本数据流图是：
 *
 *
 *
 *
 * data -> nettySelector(接收到指定端口的数据) -> decoder(将tcp数据包装成最初级的frame) -> loadBalanceHandler(进行负载均衡)
 *                                                                                               |
 *                                                                                              |
 *               frameHandler（消息内容检查和处理） <- frameDispatcher(对消息按照特定规则进行分派)    <--
 *
 *
 *
 *
 *
 */


package org.archer.archermq.protocol.transport;