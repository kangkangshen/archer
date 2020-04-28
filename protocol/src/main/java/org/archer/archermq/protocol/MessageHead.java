package org.archer.archermq.protocol;


import java.util.Map;

/**
 * 消息头
 */
public interface MessageHead {

    Map<String,Object> msgProperties();
}
