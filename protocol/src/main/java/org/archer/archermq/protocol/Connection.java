package org.archer.archermq.protocol;


import java.util.List;

/**
 * 一个网络连接，比如TCP/IP套接字连接
 * 连接设计为长期的,且可运载多个channel
 * 其生命周期设计如下：
 * 1. client打开与服务器的TCP/IP连接并发送一个协议头(protocol header).这只是client发送的数据，而不是作为方法格式的数据.
 * 2. server使用其协议版本和其它属性，包括它支持安全机制列表(Start方法)进行响应.
 * 3. client选择一种安全机制(Start-Ok).
 * 4. server开始认证过程, 它使用SASL的质询-响应模型(challenge-response model). 它向客户端发送一个质询(Secure).
 * 5. client向server发送一个认证响应(Secure-Ok). 例如，对于使用"plain"机制,响应会包含登录用户名和密码.
 * server 重复质询(Secure) 或转到协商,发送一系列参数，如最大帧大小(Tune).
 * 6. client接受或降低这些参数(Tune-Ok).
 * 7. client 正式打开连接并选择一个虚拟主机(Open).
 * 8. 服务器确认虚拟主机是一个有效的选择 (Open-Ok).
 * 9. 客户端现在使用希望的连接.
 * 10 一个节点(client 或 server) 结束连接(Close).
 * 11. 另一个节点对连接结束握手(Close-Ok).
 * 12 server 和  client关闭它们的套接字连接.
 */
public interface Connection extends LifeCycle, Registrar<String, Channel> {

    String id();

    Channel openChannel();

    /**
     * 设置当前的qos指标，-1代表忽略此次设置
     *
     * @param prefetchSize
     * @param prefetchCount
     */
    void qos(int prefetchSize, short prefetchCount);

    int getPreFetchSize();

    short getPreFetchCount();

    VirtualHost virtualHost();

}
