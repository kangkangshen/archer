package org.archer.archermq.common.log;

/**
 * 日志常量类
 *
 * @author dongyue
 * @date 2020年04月14日17:48:33
 */
public interface LogConstants {

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

    String EXCEPTION_THROW = "exception throw";

    String METHOD_INVOKE = "method invoke";


    /**
     * log content key
     */
    String CLASS_NAME = "class";

    String METHOD_NAME = "method";

    String ARGS_VAL = "args";

    String EXCEPTION_STACK = "stack";

    String CONFIG_ITEM_NAME = "config item name";

    String CONFIG_ITEM_BEFORE = "config item before";

    String CONFIG_ITEM_AFTER = "config item after";

    String REGISTERED_FRAME_HANDLER = "registered frame handler";

    String USING_FRAME_HANDLER = "using frame handler";

    String BACKUP_FRAME_HANDLER = "backup frame handler";


}
