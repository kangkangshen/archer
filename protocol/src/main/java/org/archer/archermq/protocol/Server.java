package org.archer.archermq.protocol;

/**
 * @author dongyue
 * @date 2020年04月14日13:23:50
 */
public interface Server extends LifeCycle {

    /**
     * 标准系统服务前辍是"amq."
     */
    String STANDARD_SYSTEM_SERVICE_NAME_PREFIX = "amq.";
}
