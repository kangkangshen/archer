package org.archer.archermq.protocol.constants;


/**
 * 扩展属性key常量定义
 */
public interface FeatureKeys {

    /**
     * 消息的扩展属性key常量定义,保存在msgHeader中
     */
    interface Message {

        /**
         * message priority, 0 to 9
         */
        String PRIORITY = "priority";

        String TRACE_ID = "traceId";

        String MESSAGE_KEY = "message-key";

        String MESSAGE_DESC = "message-desc";

        String EXCHANGE_NAME = "exchange";

        String ROUTING_KEY = "routingKey";

        String MANDATORY = "mandatory";

        String IMMEDIATE = "immediate";

        String REPUBLISH = "republish";

        String MESSAGE_QUEUE = "msgQueue";

        String CONTENT_TYPE = "content-type";

        String CONTENT_ENCODING = "content-encoding";

        String HEADERS = "headers";

        String DELIVERY_MODE = "delivery-mode";

        String CORRELATION_ID = "correlation-id";

        String REPLY_TO = "reply-to";

        String EXPIRATION = "expiration";

        String MESSAGE_ID = "message-id";

        String TIME_STAMP = "timestamp";

        String TYPE = "type";

        String USER_ID = "user-id";

        String APP_ID = "app-id";

        String RESERVED = "reserved";

    }

    interface Command {

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

    interface Custom {
        String EXCHANGE_ID = "exchange id";
    }


}
