package org.archer.archermq.protocol;

import org.archer.archermq.protocol.Server;
import org.archer.archermq.protocol.VirtualHost;

/**
 * 中间件服务器门面类
 *
 * @author dongyue
 * @date 2020年04月18日09:36:36
 */
public interface ServerFacade extends Server {


    VirtualHostConfig getVirtualHostByName(String name);



}
