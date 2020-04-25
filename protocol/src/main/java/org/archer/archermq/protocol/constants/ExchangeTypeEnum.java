package org.archer.archermq.protocol.constants;


import org.archer.archermq.common.EnumSpec;

import java.util.Objects;

/**
 * 基于不同路由语义的交换器类。
 *
 * @author dongyue
 * @date 2020年04月13日21:23:42
 */
public enum ExchangeTypeEnum implements EnumSpec<Integer> {
    DEFAULT(0, "default exchange"),
    DIRECT(1, "direct exchange"),
    FANOUT(2, "fanout exchange"),
    TOPIC(3, "topic exchange"),
    HEADERS(4, "heads exchange"),
    SYSTEM(5,"system exchange"),
    CUSTOM(6,"custom exchange");

    private final int val;
    private final String desc;

    ExchangeTypeEnum(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public static ExchangeTypeEnum getByVal(Integer val) {
        ExchangeTypeEnum[] types = values();
        for (ExchangeTypeEnum type : types) {
            if (Objects.equals(type.val, val)) {
                return type;
            }
        }
        return null;
    }

    public Integer getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }
}
