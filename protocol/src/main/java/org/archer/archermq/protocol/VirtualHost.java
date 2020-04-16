package org.archer.archermq.protocol;


import java.util.List;
import java.util.Set;

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
     * 对exchange的托管接口,提供对exchange的增删改查
     *
     * @author dongyue
     * @date 2020年04月16日22:59:25
     */
    interface ExchangeRegistry {

        /**
         * 获取当前exchange注册机所属的virtualHost
         *
         * @return 当前exchange注册机所属的virtualHost
         */
        VirtualHost virtualHost();

        /**
         * 往当前exchange注册机注册一个exchange,如果存在同名的exchange，将会覆盖掉
         * 如果为exchange为null,将忽略此次注册
         *
         * @param exchange 欲注册的exchange
         */
        void registerExchange(Exchange exchange);

        /**
         * 当前exchange注册机删除掉指定name的exchange
         *
         * @param name 欲删除的exchange的name
         */
        void removeExchange(String name);

        /**
         * 检查当前exchange注册机是否包含指定name的exchange
         *
         * @param name exchange的名称
         * @return true if 存在 , false if不存在
         */
        boolean containsExchange(String name);

        /**
         * 从当前exchange注册机获取指定name的exchange
         *
         * @param name 指定exchange的name
         * @return 指定name的exchange
         */
        Exchange getExchange(String name);

        Set<String> exchangeNames();

        List<Exchange> exchanges();

    }

    /**
     *
     */
    interface ExchangeRouter {

    }


}
