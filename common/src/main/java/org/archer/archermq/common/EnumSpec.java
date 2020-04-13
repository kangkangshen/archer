package org.archer.archermq.common;


/**
 * 枚举规范,定义的枚举需要都实现该接口
 *
 * @author dongyue
 * @date 2020年04月13日22:43:19
 */
public interface EnumSpec<T> {

    /**
     * 获取枚举值
     *
     * @return 当前枚举唯一值
     */
    T getVal();

    /**
     * 获取枚举描述
     *
     * @return 当前枚举的描述
     */
    String getDesc();
}
