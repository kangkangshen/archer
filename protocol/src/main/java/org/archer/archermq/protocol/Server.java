package org.archer.archermq.protocol;

/**
 * 作为整个中间件的入口，提供了接受客户端连接，实现AMQP消息队列和路由功能的过程,管理virtualHost,
 *
 *
 * 1.如果配置了archermq.server.facade = true , 那么本服务器将作为整个中间件的管理器,通常只有集群leader该值被设置成true
 * 当leader挂掉时，由，
 *  提供 根据virtualHostName返回一个actualHostNames
 *
 *
 * @author dongyue
 * @date 2020年04月14日13:23:50
 */
public interface Server extends LifeCycle {

    /**
     * 标准系统服务前辍是"amq."
     */
    String STANDARD_SYSTEM_SERVICE_NAME_PREFIX = "amq.";

    void start();

    void destroy();

}
