package org.archer.archermq.protocol.constants;


/**
 * 扩展属性key常量定义
 */
public interface FeatureKeys {

    /**
     * 消息的扩展属性key常量定义,保存在msgHeader中
     */
    interface Message{

        String PRIORITY = "priority";

        String TRACE_ID = "traceId";

        String EXCHANGE_NAME = "exchange name";

        String ROUTING_KEY = "routing key";
    }

    interface Command{

        String TCP_CHANNEL = "tcp channel";

        String AMQP_CONNECTION = "amqp connection";

        String AMQP_CHANNEL = "amqp channel";

        String CONN_MNG = "connMng";

        String PEER_IP = "ip";

        String PEER_PORT = "port";

        String VIRTUALHOST_NAME = "virtualHost name";

        String VIRTUALHOST = "virtualHost";

        String SERVER = "server";
    }

    interface Custom{
        String EXCHANGE_ID = "exchange id";
    }







}
