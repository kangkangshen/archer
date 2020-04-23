package org.archer.archermq.protocol.constants;

import org.archer.archermq.common.EnumSpec;

/**
 * 类定义
 *
 * @author dongyue
 * @date 2020年04月23日09:20:00
 */
public enum ClassEnum implements EnumSpec<Integer> {
    CONNECTION(10, "work with socket connections"),
    CHANNEL(20, "work with channels"),
    EXCHANGE(40, "work with exchanges"),
    QUEUE(50, "work with queues"),
    BASIC(60, "work with basic content"),
    TX(90, "work with transactions");


    private final int val;
    private final String desc;

    ClassEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    @Override
    public Integer getVal() {
        return val;
    }

    @Override
    public String getDesc() {
        return desc;
    }
}
