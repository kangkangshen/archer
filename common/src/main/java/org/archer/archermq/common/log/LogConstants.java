package org.archer.archermq.common.log;

/**
 * 日志常量类
 *
 * @author dongyue
 * @date 2020年04月14日17:48:33
 */
public interface LogConstants {

    /**
     * logger
     */
    String TRACE = "trace";
    String SYS_ERR = "sys_err";

    /**
     * layer
     */
    String TRANSPORT_LAYER = "transport layer";

    String SESSION_LAYER = "session layer";

    String MODEL_LAYER = "model layer";

    /**
     * log type
     */
    String CONFIG_CHANGED = "config changed";

    String INSTANCE_CREATED = "instance created";

    String INSTANCE_DESTROYED = "instance destroyed";

    String EXCEPTION_THROW = "exception throw";

    String METHOD_INVOKE = "method invoke";

    String CONNECTION_CREATED = "connection created";

    String CONNECTION_CLOSED = "connection closed";

    String CHANNEL_CREATED = "channel created";

    String CHANNEL_CLOSED = "channel closed";

    String HEARTBEAT = "heartbeat";


    /**
     * log content key
     */
    String CLASS_NAME = "class";

    String METHOD_NAME = "method";

    String ARGS_VAL = "args";

    String EXCEPTION_STACK = "stack";

    String INSTANCE = "instance";

    String CONFIG_ITEM_NAME = "config item name";

    String CONFIG_ITEM_BEFORE = "config item before";

    String CONFIG_ITEM_AFTER = "config item after";

    String REGISTERED_FRAME_HANDLER = "registered frame handler";

    String USING_FRAME_HANDLER = "using frame handler";

    String BACKUP_FRAME_HANDLER = "backup frame handler";

    String RESPONSE = "response";

    String PEER_IP = "ip";

    String PEER_PORT = "port";

    String VIRTUALHOST_NAME = "virtual host name";

    String REPLY_CODE = "reply code";

    String REPLY_TEXT = "reply text";

    String TIME_STAMP = "timestamp";


}
