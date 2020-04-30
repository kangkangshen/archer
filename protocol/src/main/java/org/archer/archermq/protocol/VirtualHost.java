package org.archer.archermq.protocol;


import io.netty.channel.Channel;
import org.archer.archermq.common.Namespace;
import org.archer.archermq.config.register.Registrar;

import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 虚拟主机由它自己的名字空间和一组交换器、消息队列和所有关联的对象组成。每个连接(Connection)必须和某个虚拟主机关联。
 * 一个虚拟主机可以是一个集群，多个集群聚合在一起的新的集群，甚至是就是一台主机
 *
 * @author dongyue
 * @date 2020年04月13日21:53:16
 */
public interface VirtualHost extends LifeCycle {

    /**
     * 获取当前虚拟主机的命名空间
     *
     * @return 命名空间
     */
    Namespace nameSpace();

    /**
     * 获取当前虚拟主机的名字，一般来说，我们通过namespace+name唯一确定一个virtualHost
     *
     * @return 当前虚拟主机的名字
     */
    String name();

    /**
     * 一般来说，我们通过我们通过namespace+name唯一确定一个virtualHost，name是具有一定含义的，id只具备索引功能
     * 默认情况下，id就是name
     *
     * @return 当前virtualHost的id
     */
    default String id() {
        return name();
    }


    /**
     * 获取当前虚拟主机所关联的交换器列表
     *
     * @return 当前虚拟主机所关联的交换器列表
     */
    List<Exchange> exchanges();

    /**
     * 死信队列
     *
     * @return
     */
    MessageQueue deadLetteredQueue();




    Registrar<String, Exchange> getExchangeRegistry();

    Registrar<String, MessageQueue> getMsgQueueRegistry();

    Registrar<Channel,Connection> getConnRegistry();

    ExecutorService taskPool();


}
