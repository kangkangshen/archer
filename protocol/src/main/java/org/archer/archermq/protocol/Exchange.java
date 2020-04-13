package org.archer.archermq.protocol;


import org.archer.archermq.protocol.constants.ExchangeType;

/**
 * @author dongyue
 * @date 2020年04月13日18:38:47
 */
public interface Exchange extends LifeCycle{

    /**
     * 获取当前的交换器的类型
     *
     * @return 当前交换器的类型
     * @see ExchangeType
     */
    ExchangeType type();




}
